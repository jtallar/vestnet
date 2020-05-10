package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.MessageAlreadySentException;
import ar.edu.itba.paw.interfaces.MessageDao;
import ar.edu.itba.paw.model.Message;
import org.simpleflatmapper.jdbc.spring.JdbcTemplateMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

@Repository
public class MessageJdbcDao implements MessageDao {

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    private final static ResultSetExtractor<List<Message>> RESULT_SET_EXTRACTOR = JdbcTemplateMapperFactory
            .newInstance()
            .addKeys("id")
            .newResultSetExtractor(Message.class);

    @Autowired
    public MessageJdbcDao (final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(JdbcQueries.MESSAGE_TABLE)
                .usingGeneratedKeyColumns("id")
                .usingColumns("content_message", "content_offer", "content_interest", "sender_id", "receiver_id", "project_id");
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public long create(String message, String offer, String interest, long senderId, long receiverId, long projectId) throws MessageAlreadySentException {
        // Check if last message was answered
        Optional<Message> lastMessage = findLastSentMessage(senderId, receiverId, projectId);
        if (lastMessage.isPresent() && lastMessage.get().isAccepted() == null) throw new MessageAlreadySentException();

        Map<String, Object> values = new HashMap<>();
        if (message.length() > 0) values.put("content_message", message);
        values.put("content_offer",offer);
        if (interest.length() > 0) values.put("content_interest", interest);
        values.put("sender_id", senderId);
        values.put("receiver_id", receiverId);
        values.put("project_id", projectId);
        return jdbcInsert.executeAndReturnKey(values).longValue();
    }

    @Override
    public List<Message> getConversation(long entrepreneurId, long investorId, long projectId) {
        return jdbcTemplate.query(JdbcQueries.MESSAGE_GET_CONVERSATION, RESULT_SET_EXTRACTOR, projectId, entrepreneurId, investorId, investorId, entrepreneurId);
    }

    @Override
    public List<Message> getProjectUnread(long userId, long projectId) {
        return jdbcTemplate.query(JdbcQueries.MESSAGE_GET_PROJECT_UNREAD, RESULT_SET_EXTRACTOR, projectId, userId);
    }

    @Override
    public long updateMessageStatus(long senderId, long receiverId, long projectId, boolean accepted) {
        return jdbcTemplate.update(JdbcQueries.MESSAGE_UPDATE_STATUS, accepted, senderId, receiverId, projectId);
    }

    /**
     * Finds the last message sent within a project.
     * @param senderId Unique user sender id.
     * @param receiverId Unique user receiver id.
     * @param projectId Unique project id.
     * @return The last message or null if none
     */
    private Optional<Message> findLastSentMessage(long senderId, long receiverId, long projectId) {
        return jdbcTemplate.query(JdbcQueries.MESSAGE_GET_SENT_TO, RESULT_SET_EXTRACTOR, projectId, senderId, receiverId)
                .stream().findFirst();
    }


    @Override
    public List<Message> getAccepted( long receiver_id, long from, long to) {
        List<Integer> ids = jdbcTemplate.queryForList(JdbcQueries.MESSAGE_ACCEPTED_ID, new Object[] { receiver_id, from, to}, Integer.class);
        List<Message> messages;
        if(!ids.isEmpty()) {
            MapSqlParameterSource params = new MapSqlParameterSource().addValue("ids", ids);
            messages = namedParameterJdbcTemplate.query(JdbcQueries.MESSAGE_GET_ID_LIST, params, RESULT_SET_EXTRACTOR);
        }
        else {
            messages = new ArrayList<>();
        }
        return messages;
    }

    @Override
    public Integer countAccepted(long receiver_id) {

        Integer count = jdbcTemplate.queryForObject(JdbcQueries.MESSAGE_ACCEPTED_COUNT, new Object[] {receiver_id}, Integer.class);
        return count;
    }
}
