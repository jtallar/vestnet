package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.MessageService;
import ar.edu.itba.paw.model.Message;

import java.util.List;

public class MessageServiceImpl implements MessageService {

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
