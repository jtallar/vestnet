package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.MessageDao;
import ar.edu.itba.paw.interfaces.exceptions.MessageAlreadySentException;
import ar.edu.itba.paw.model.Message;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class MessageJpaDao implements MessageDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public long create(String message, String offer, String interest, long senderId, long receiverId, long projectId) throws MessageAlreadySentException {
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

    @Override
    public List<Message> getAccepted(long receiver_id, long from, long to) {
        return null;
    }

    @Override
    public Integer countAccepted(long receiver_id) {
        return null;
    }

    @Override
    public List<Message> getOffersDone(long sender_id, long from, long to) {
        return null;
    }

    @Override
    public Integer countOffers(long sender_id) {
        return null;
    }
}
