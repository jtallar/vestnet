package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.daos.MessageDao;
import ar.edu.itba.paw.interfaces.services.MessageService;
import ar.edu.itba.paw.model.Message;
import ar.edu.itba.paw.model.Message.MessageContent;
import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.components.FilterCriteria;
import ar.edu.itba.paw.model.components.OrderField;
import ar.edu.itba.paw.model.components.Page;
import ar.edu.itba.paw.model.components.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Primary
@Service
public class IMessageService implements MessageService {

    @Autowired
    private MessageDao messageDao;


    @Override
    @Transactional
    public Message create(String message, int offer, String interest, long senderId, long receiverId, long projectId) {
        MessageContent content = new MessageContent(message, String.valueOf(offer), interest);
        return messageDao.create(content, new User(senderId), new User(receiverId), new Project(projectId));
    }


    @Override
    @Transactional
    public Message updateMessageStatus(long senderId, long receiverId, long projectId, boolean accepted) {
        List<FilterCriteria> filters = new ArrayList<>();
        filters.add(new FilterCriteria("sender", new User(senderId)));
        filters.add(new FilterCriteria("receiver", new User(receiverId)));
        filters.add(new FilterCriteria("project", new Project(projectId)));
        filters.add(new FilterCriteria("unread", true));

        Optional<Message> optionalMessage = messageDao.findAll(filters, OrderField.DEFAULT).stream().findFirst();
        if (!optionalMessage.isPresent()) return null;

        Message message = optionalMessage.get();
        message.setAccepted(accepted);
        return message;
    }
}