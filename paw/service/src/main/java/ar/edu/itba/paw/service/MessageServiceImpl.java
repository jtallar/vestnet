package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.daos.MessageDao;
import ar.edu.itba.paw.interfaces.services.EmailService;
import ar.edu.itba.paw.interfaces.services.MessageService;
import ar.edu.itba.paw.interfaces.services.ProjectService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.Message;
import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.components.*;
import ar.edu.itba.paw.model.enums.GroupField;
import ar.edu.itba.paw.model.enums.OrderField;
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
    public Page<Message> getProjectInvestors(long projectId, long ownerId, boolean accepted, int page, int pageSize) {
        MessageRequestBuilder request = new MessageRequestBuilder()
                .setOwner(ownerId)
                .setProject(projectId)
                .setOrder(OrderField.DATE_DESCENDING);

        if (accepted) request.setAccepted();
        else request.setGroup(GroupField.INVESTOR);

        return messageDao.findAll(request, new PageRequest(page, pageSize));
    }


    @Override
    public Page<Message> getInvestorProjects(long investorId, boolean accepted, int page, int pageSize) {
        MessageRequestBuilder request = new MessageRequestBuilder()
                .setInvestor(investorId)
                .setOrder(OrderField.DATE_DESCENDING);

        if (accepted) request.setAccepted();
        else request.setGroup(GroupField.PROJECT);

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

        /** Set message as accepted or not, and if accepted add the new funds */
        message.setAccepted(accepted);
        message.setSeen();
        project.get().setFundingCurrent(project.get().getFundingCurrent() + message.getContent().getOffer());

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


    @Override
    public long projectNotifications(long projectId, long ownerId) {
        RequestBuilder request = new MessageRequestBuilder()
                .setOwner(ownerId)
                .setProject(projectId)
                .setUnseen()
                .setFromInvestor()
                .setOrder(OrderField.DATE_DESCENDING)
                .setGroup(GroupField.INVESTOR);

        return messageDao.countAll(request);
    }

    @Override
    public long userNotifications(long sessionUserId, boolean isInvestor) {
        /** If the user is an entrepreneur */
        if (!isInvestor)
            return messageDao.countEntrepreneurNotifications(sessionUserId);

        RequestBuilder request = new MessageRequestBuilder()
                .setInvestor(sessionUserId)
                .setUnseen()
                .setFromEntrepreneur()
                .setOrder(OrderField.DATE_DESCENDING)
                .setGroup(GroupField.PROJECT);

        return messageDao.countAll(request);
    }


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
        message.setSeen();
        message.setAccepted(false);
        return true;

    }

}