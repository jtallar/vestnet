package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.MessageDao;
import ar.edu.itba.paw.interfaces.exceptions.MessageAlreadySentException;
import ar.edu.itba.paw.model.Message;
import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.components.Page;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class MessageJpaDao implements MessageDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Message create(Message.MessageContent content, User sender, User receiver, Project project) throws MessageAlreadySentException {
        // TODO check this how are we going to do it
        if(project == null) throw new MessageAlreadySentException();

        final Message message = new Message(content, sender, receiver, project);
        entityManager.persist(message);
        return message;
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
    public Page<Message> getAccepted(long receiver_id, long from, long to) {
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
