package ar.edu.itba.paw.service;

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

    @Override
    public long create(String message, String offer, String interest, long senderId, long receiverId, long projectId) {
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
}
