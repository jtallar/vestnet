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
import ar.edu.itba.paw.model.components.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
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
    public Optional<Message> create(Message messageData, long sessionUserId, URI baseUri) {

        /** Checks it the session user id is from one of the two users negotiating, and sets direction of message */
        if (sessionUserId == messageData.getInvestorId())
            messageData.setDirection(true);
        else if (sessionUserId == messageData.getOwnerId())
            messageData.setDirection(false);
        else return Optional.empty();

        /** Should be two different users */
        if (messageData.getInvestorId() == messageData.getOwnerId()) return Optional.empty();

        /** Checks for the existence of the project and the owner ID is the right one */
        Optional<Project> project = projectService.findById(messageData.getProjectId());
        if (!project.isPresent() || project.get().getOwnerId() != messageData.getOwnerId()) return Optional.empty();

        /** Checks if both users exists */
        Optional<User> owner = userService.findById(messageData.getOwnerId());
        Optional<User> investor = userService.findById(messageData.getProjectId());
        if (!owner.isPresent() || !investor.isPresent()) return Optional.empty();

        /** Adds one message to the project*/
        project.get().addMsgCount();

        /** Persists message */
        Message finalMessage = messageDao.create(messageData);

        /** Sends email */
        emailService.sendOffer(owner.get(), investor.get(), project.get(), messageData.getContent(), baseUri);

        return Optional.of(finalMessage);
    }


    @Override
    @Transactional
    public Optional<Message> updateMessageStatus(long senderId, long receiverId, long projectId, boolean accepted, URI baseUri) {
        RequestBuilder request = new MessageRequestBuilder()
                .setInvestor(senderId)
                .setOwner(receiverId)
                .setProject(projectId)
                .setSeen()
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

    @Override
    public Page<Message> getProjectInvestors(long projectId, long ownerId, int page, int pageSize) {
        RequestBuilder request = new MessageRequestBuilder()
                .setOwner(ownerId)
                .setProject(projectId)
                .setOrder(OrderField.DATE_DESCENDING); // TODO add to group by investor

        return messageDao.findAll(request, new PageRequest(page, pageSize));
    }
}