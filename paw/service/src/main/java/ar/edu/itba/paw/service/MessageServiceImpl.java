package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.exceptions.MessageAlreadySentException;
import ar.edu.itba.paw.interfaces.daos.MessageDao;
import ar.edu.itba.paw.interfaces.services.MessageService;
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
        long intpage = Long.parseLong(page);
        long from = (intpage - 1) * PAGE_SIZE;
        return messageDao.getOffersDone(sender_id,from,to);
    }

    @Override
    public Integer countOffers(long sender_id) {
        return messageDao.countOffers(sender_id);
    }

    @Override
    public Boolean hasNextRequest(String page, long id) {
        int intpage = Integer.parseInt(page);
        int count = countOffers(id);
        return count > ((intpage)* PAGE_SIZE);
    }

    @Override
    public Boolean hasNextDeal(String page, long id) {
        int intpage = Integer.parseInt(page);
        int count = countAccepted(id);
        return count > ((intpage)* PAGE_SIZE);
    }

    @Override
    public Integer getPageSize() {
        return PAGE_SIZE;
    }
}
