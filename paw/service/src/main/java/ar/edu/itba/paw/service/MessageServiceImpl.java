package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.MessageAlreadySentException;
import ar.edu.itba.paw.interfaces.MessageDao;
import ar.edu.itba.paw.interfaces.MessageService;
import ar.edu.itba.paw.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Primary
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageDao messageDao;

    private final Integer PAGE_SIZE = 10;

    @Override
    public long create(String message, String offer, String interest, long senderId, long receiverId, long projectId) throws MessageAlreadySentException {
        return messageDao.create(message, offer, interest, senderId, receiverId, projectId);
    }

    @Override
    public List<Message> getConversation(long entrepreneurId, long investorId, long projectId) {
        return messageDao.getConversation(entrepreneurId, investorId, projectId);
    }

    @Override
    public List<Message> getProjectUnread(long userId, long projectId) {
        return messageDao.getProjectUnread(userId, projectId);
    }

    @Override
    public long updateMessageStatus(long senderId, long receiverId, long projectId, boolean accepted) {
        return messageDao.updateMessageStatus(senderId, receiverId, projectId, accepted);
    }

    @Override
    public List<Message> getAccepted( long receiver_id,String page, long to) {
        int from = (Integer.parseInt(page) - 1) * PAGE_SIZE;
        return messageDao.getAccepted(receiver_id,from,to);
    }

    @Override
    public Integer countAccepted( long receiver_id) {
        return messageDao.countAccepted(receiver_id);
    }

    @Override
    public List<Message> getOffersDone(long sender_id, String page, long to) {
        Long intpage = Long.parseLong(page);
        Long from = (intpage - 1) * PAGE_SIZE;
        return messageDao.getOffersDone(sender_id,from,to);
    }

    @Override
    public Integer countOffers(long sender_id) {
        return messageDao.countOffers(sender_id);
    }

    @Override
    public Boolean hasNextRequest(String page, long id) {
        Integer intpage = Integer.parseInt(page);
        Integer count = countOffers(id);
        Boolean hasNext = count > ((intpage)* PAGE_SIZE);
        return hasNext;
    }

    @Override
    public Boolean hasNextDeal(String page, long id) {
        Integer intpage = Integer.parseInt(page);
        Integer count = countAccepted(id);
        Boolean hasNext = count > ((intpage)* PAGE_SIZE);
        return hasNext;
    }

    @Override
    public Integer getPageSize() {
        return PAGE_SIZE;
    }
}
