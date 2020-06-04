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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Primary
@Service
public class IMessageService implements MessageService {

    @Autowired
    private MessageDao messageDao;


    @Override
    public Message create(String message, int offer, String interest, long senderId, long receiverId, long projectId) {
        MessageContent content = new MessageContent(message, String.valueOf(offer), interest);
        return messageDao.create(content, new User(senderId), new User(receiverId), new Project(projectId));
    }

    @Override
    public Page<Message> getUserAccepted(long receiverId, Integer page, Integer pageSize) {
        List<FilterCriteria> filters = new ArrayList<>();
        filters.add(new FilterCriteria("receiver", new User(receiverId)));
        filters.add(new FilterCriteria("accepted", true));
        return messageDao.findAll(filters, OrderField.DATE_DESCENDING, new PageRequest(page, pageSize));
    }

    @Override
    public Page<Message> getUserOffers(long senderId, Integer page, Integer pageSize) {
        List<FilterCriteria> filters = Collections.singletonList(new FilterCriteria("sender", new User(senderId)));
        return messageDao.findAll(filters, OrderField.DATE_DESCENDING, new PageRequest(page, pageSize));
    }

    @Override
    public Page<Message> getConversation(long receiverId, long senderId, long projectId, Integer page, Integer pageSize) {
        List<FilterCriteria> filters = new ArrayList<>();
        filters.add(new FilterCriteria("receiver", new User(receiverId)));
        filters.add(new FilterCriteria("receiver", new User(senderId)));
        filters.add(new FilterCriteria("sender", new User(receiverId)));
        filters.add(new FilterCriteria("sender", new User(senderId)));
        filters.add(new FilterCriteria("project", new Project(projectId)));
        return messageDao.findAll(filters, OrderField.DATE_DESCENDING, new PageRequest(page, pageSize));
    }

    @Override
    public List<Message> getUserProjectUnread(long userId, long projectId) {
        List<FilterCriteria> filters = new ArrayList<>();
        filters.add(new FilterCriteria("receiver", new User(userId)));
        filters.add(new FilterCriteria("project", new Project(projectId)));
        filters.add(new FilterCriteria("unread", true));
        return messageDao.findAll(filters, OrderField.DATE_DESCENDING);
    }

    @Override
    public Optional<Message> getUserProjectLast(long userId, long projectId) {
        List<FilterCriteria> filters = new ArrayList<>();
        filters.add(new FilterCriteria("sender", new User(userId)));
        filters.add(new FilterCriteria("project", new Project(projectId)));
        return messageDao.findAll(filters, OrderField.DATE_DESCENDING).stream().findFirst();
    }

    @Override
    public Message updateMessageStatus(long senderId, long receiverId, long projectId, boolean accepted) {
        List<FilterCriteria> filters = new ArrayList<>();
        filters.add(new FilterCriteria("sender", new User(senderId)));
        filters.add(new FilterCriteria("receiver", new User(receiverId)));
        filters.add(new FilterCriteria("project", new Project(projectId)));
        filters.add(new FilterCriteria("unread", true));
        return messageDao.updateMessageStatus(filters, accepted);
    }
}
