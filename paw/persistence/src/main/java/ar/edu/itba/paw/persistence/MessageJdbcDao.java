package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.MessageAlreadySentException;
import ar.edu.itba.paw.interfaces.MessageDao;
import ar.edu.itba.paw.model.Message;
import ar.edu.itba.paw.model.User;
import org.simpleflatmapper.jdbc.spring.JdbcTemplateMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MessageJdbcDao implements MessageDao {

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

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
    }

    @Override
    public long create(String message, String offer, String interest, long senderId, long receiverId, long projectId) throws MessageAlreadySentException {
        Message lastMessage = findMessage(senderId, receiverId, projectId);
        if (lastMessage != null && lastMessage.isAccepted() == null) throw new MessageAlreadySentException();
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

    private Message findMessage(long senderId, long receiverId, long projectId) {
        List<Message> messages= jdbcTemplate.query(JdbcQueries.MESSAGE_GET_SENT_TO, RESULT_SET_EXTRACTOR, projectId, senderId, receiverId);
        if (messages.size() == 0) return null;
        return messages.get(messages.size() - 1);
    }
}
