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

import javax.swing.text.html.Option;
import java.net.URI;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
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

    private static final int OFFER_EXPIRY_DAYS = 5;


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
        Optional<User> investor = userService.findById(messageData.getInvestorId());
        if (!owner.isPresent() || !investor.isPresent()) return Optional.empty();

        /** Checks if the user is able to sent message */
        if (!isPostOfferValid(messageData.getOwnerId(), messageData.getInvestorId(), messageData.getProjectId(), messageData.getDirection()))
            return Optional.empty();

        /** Persists message */
        Message finalMessage = messageDao.create(messageData);

        /** Adds one message to the project*/
        project.get().addMsgCount();

        /** Sends email */
        emailService.sendOffer(owner.get(), investor.get(), project.get(), messageData.getContent(), messageData.getDirection(), baseUri);

        return Optional.of(finalMessage);
    }


    @Override
    public Optional<Message> getLastChatMessage(long projectId, long investorId, long sessionUserId) {
        RequestBuilder request = new MessageRequestBuilder()
                .setProject(projectId)
                .setInvestor(investorId)
                .setOwner(sessionUserId, (sessionUserId != investorId))
                .setOrder(OrderField.DATE_DESCENDING);

        return messageDao.findAll(request).stream().findFirst();
    }

    @Override
    public Page<Message> getProjectInvestors(long projectId, long ownerId, int page, int pageSize) {
        RequestBuilder request = new MessageRequestBuilder()
                .setOwner(ownerId)
                .setProject(projectId)
                .setOrder(OrderField.DATE_DESCENDING); // TODO add to group by investor

        return messageDao.findAll(request, new PageRequest(page, pageSize));
    }


    @Override
    public Page<Message> getInvestorProjects(long investorId, int page, int pageSize) {
        RequestBuilder request = new MessageRequestBuilder()
                .setInvestor(investorId)
                .setOrder(OrderField.DATE_DESCENDING); // TODO add to group by project

        return messageDao.findAll(request, new PageRequest(page, pageSize));
    }


    @Override
    public Page<Message> getConversation(long projectId, long investorId, long sessionUserId, int page, int pageSize) {
        RequestBuilder request = new MessageRequestBuilder()
                .setProject(projectId)
                .setInvestor(investorId)
                /** If session user is the investor then don't need to check project ownership */
                .setOwner(sessionUserId, (sessionUserId != investorId))
                .setOrder(OrderField.DATE_DESCENDING);

        return messageDao.findAll(request, new PageRequest(page, pageSize));
    }


    @Override
    @Transactional
    public Optional<Message> updateMessageStatus(long projectId, long investorId, long sessionUserId, boolean accepted, URI baseUri) {
        Optional<Message> optionalMessage = getLastChatMessage(projectId, investorId, sessionUserId);

        if (!optionalMessage.isPresent()) return Optional.empty();
        Message message = optionalMessage.get();


        Optional <Project> project = projectService.findById(projectId);
        Optional<User> owner = userService.findById(message.getOwnerId());
        Optional<User> investor = userService.findById(investorId);

        /** This cannot happen as the message if its on the database, then the owner, investor and project exist */
        if (!project.isPresent() || !owner.isPresent() || !investor.isPresent()) return Optional.empty();

        /** If the message has expired */
        if (!message.isExpiryDateValid()) {
            // TODO send mail offer has expired?
            message.setAccepted(false);
            return Optional.empty();
        }

        /** Valid expire date */
        /** Is investor, last message cannot be his */
        if (sessionUserId == investorId && message.getDirection())
            return Optional.empty();

        /** Is entrepreneur, last message cannot be his */
        if (sessionUserId == message.getOwnerId() && !message.getDirection())
            return Optional.empty();

        /** Set message as accepted or not */
        message.setAccepted(accepted);

        /** Send email */
        emailService.sendOfferAnswer(owner.get(), investor.get(), project.get(), accepted, message.getDirection(), baseUri);
        return optionalMessage;
    }

    @Override
    @Transactional
    public Optional<Message> updateMessageSeen(long projectId, long investorId, long sessionUserId, URI baseUri) {
        Optional<Message> optionalMessage = getLastChatMessage(projectId, investorId, sessionUserId);
        optionalMessage.ifPresent(m -> {

            /** Is investor, last message cannot be his */
            if (sessionUserId == investorId && m.getDirection()) return;

            /** Is entrepreneur, last message cannot be his */
            if (sessionUserId == m.getOwnerId() && !m.getDirection()) return;

            m.setSeen();
        });

        return optionalMessage;
    }


    // previously user service impl
//    @Override
//    public Page<Message> getAcceptedMessages(long receiverId, int page, int pageSize) {
//        RequestBuilder request = new MessageRequestBuilder()
//                .setOwner(receiverId)
//                .setAccepted(true)
//                .setOrder(OrderField.DATE_DESCENDING);
//        return messageDao.findAll(request, new PageRequest(page, pageSize));
//    }
//
//
//    @Override
//    public Page<Message> getOfferMessages(long senderId, int page, int pageSize) {
//        RequestBuilder request = new MessageRequestBuilder()
//                .setInvestor(senderId)
//                .setOrder(OrderField.DATE_DESCENDING);
//        return messageDao.findAll(request, new PageRequest(page, pageSize));
//    }
//
//
//    @Override
//    public List<Message> getProjectUnreadMessages(long userId, long projectId) {
//        RequestBuilder request = new MessageRequestBuilder()
//                .setOwner(userId)
//                .setProject(projectId)
//                .setSeen()
//                .setOrder(OrderField.DATE_DESCENDING);
//        return messageDao.findAll(request);
//    }
//
//
//    @Override
//    public Optional<Message> getLastProjectOfferMessage(long userId, long projectId) {
//        RequestBuilder request = new MessageRequestBuilder()
//                .setInvestor(userId)
//                .setProject(projectId)
//                .setOrder(OrderField.DATE_DESCENDING);
//        return messageDao.findAll(request).stream().findFirst();
//    }

    /** Auxiliary Methods */

    /**
     * Checks if the user is able to make the post offer message or not. Based on the last previous message.
     * @param ownerId The owner's unique ID.
     * @param investorId The investor's unique ID.
     * @param projectId The project's unique ID.
     * @param direction The direction of the conversation given. True for investor to entrepreneur. False otherwise.
     * @return True if its a valid request, false otherwise.
     */
    private boolean isPostOfferValid(long ownerId, long investorId, long projectId, boolean direction) {
        RequestBuilder request = new MessageRequestBuilder()
                .setOwner(ownerId)
                .setInvestor(investorId)
                .setProject(projectId)
                .setOrder(OrderField.DATE_DESCENDING);

        Optional<Message> lastMessage = messageDao.findAll(request).stream().findFirst();

        /** Opening of a new negotiation, only the investors  */
        if (!lastMessage.isPresent())
            return direction;

        Message message = lastMessage.get();

        /** Middle of negotiation */
        /** If it's accepted, then only the investor can start a new negotiation */
        if (message.getAccepted())
            return direction;

        /** Rejected the last message, both can send a new one */
        if (!message.getAccepted())
            return true;

        /** Messages that are not accepted or rejected */

        /** With an expiry date not yet crossed, sent only if not the last one */
        if (message.isExpiryDateValid())
            return message.getDirection() != direction;

        /** With an expiry date has expired, set the offer as rejected */
        message.setAccepted(false);
        // TODO sent mail the offer has expired?
        return true;

    }

}