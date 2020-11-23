package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.daos.MessageDao;
import ar.edu.itba.paw.interfaces.services.EmailService;
import ar.edu.itba.paw.interfaces.services.MessageService;
import ar.edu.itba.paw.interfaces.services.ProjectService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.Message;
import ar.edu.itba.paw.model.Message.MessageContent;
import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.components.FilterCriteria;
import ar.edu.itba.paw.model.components.MessageRequestBuilder;
import ar.edu.itba.paw.model.components.OrderField;
import ar.edu.itba.paw.model.components.RequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Primary
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;


    @Override
    @Transactional
    public Message create(String message, String offer, String interest, long senderId, long receiverId, long projectId, URI baseUri) {
        MessageContent content = new MessageContent(message, offer, interest);
        Message finalMessage = messageDao.create(content, new User(senderId), new User(receiverId), new Project(projectId));

        Optional<User> sender = userService.findById(senderId);
        Optional<User> receiver = userService.findById(receiverId);
        Optional<Project> project = projectService.addMsgCount(projectId);
        if (!sender.isPresent() || !receiver.isPresent() || !project.isPresent()) return null; // TODO this. What happens if not exists one of them
        emailService.sendOffer(sender.get(), receiver.get(), project.get(), content, baseUri);

        return finalMessage;
    }


    @Override
    @Transactional
    public Optional<Message> updateMessageStatus(long senderId, long receiverId, long projectId, boolean accepted, URI baseUri) {
        RequestBuilder request = new MessageRequestBuilder()
                .setSender(senderId)
                .setReceiver(receiverId)
                .setProject(projectId)
                .setUnread(true)
                .setOrder(OrderField.DEFAULT);

        Optional<Message> optionalMessage = messageDao.findAll(request).stream().findFirst();
        optionalMessage.ifPresent(m -> {
            m.setAccepted(accepted);
            Optional<User> sender = userService.findById(senderId);
            Optional<User> receiver = userService.findById(receiverId);
            Optional <Project> project = projectService.decMsgCount(projectId);
            if (!sender.isPresent() || !receiver.isPresent() || !project.isPresent()) return; // TODO this. What happens if not exists one of them

            emailService.sendOfferAnswer(sender.get(), receiver.get(), project.get(), accepted, baseUri);
        });

        return optionalMessage;
    }
}