package ar.edu.itba.paw.persistence;

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
import java.util.List;

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
                .withTableName(JdbcQueries.USER_TABLE)
                .usingGeneratedKeyColumns("id")
                .usingColumns("content_message", "content_offer", "content_interest", "sender_id", "receiver_id", "project_id");
    }

    @Override
    public long create(String message, String offer, String interest, long senderId, long receiverId, long projectId) {
        return 0;
    }

    @Override
    public List<Message> getConversation(long entrepreneurId, long investorId, long projectId) {
        return null;
    }

    @Override
    public List<Message> getProjectUnread(long userId, long projectId) {
        return null;
    }

    @Override
    public long updateMessageStatus(long senderId, long receiverId, long projectId, boolean accepted) {
        return 0;
    }
}
