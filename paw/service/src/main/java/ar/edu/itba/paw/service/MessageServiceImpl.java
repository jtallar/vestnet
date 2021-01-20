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
    public Optional<Message> create(long projectId, long investorId, long sessionUserId, Message.MessageContent content, int expiryDays, URI baseUri) {

        /** Checks for the existence of the project and the owner ID is the right one */
        Optional<Project> project = projectService.findById(projectId);
        if (!project.isPresent()) return Optional.empty();

        /** Checks it the session user id is from one of the two users negotiating, and sets direction of message */
        boolean direction;
        if (sessionUserId == investorId)
            direction = true;
        else if (sessionUserId == project.get().getOwnerId())
            direction = false;
        else return Optional.empty();

        /** Checks if both users exists */
        Optional<User> owner = userService.findById(project.get().getOwnerId());
        Optional<User> investor = userService.findById(investorId);
        if (!owner.isPresent() || !investor.isPresent()) return Optional.empty();

        /** Checks if the user is able to sent message */
        if (!isPostOfferValid(owner.get().getId(), investorId, projectId, direction))
            return Optional.empty();

        /** Creates the message data to persist */
        Message messageData = new Message(content, new User(project.get().getOwnerId()),
                new User(investorId), new Project(projectId), direction, expiryDays);

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
        if (accepted)
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

            /** Is investor, last message is his. If accepted or rejected then set as seen answer */
            if (sessionUserId == investorId && m.getDirection()) {
                if (m.getAccepted() != null)
                    m.setSeenAnswer();
                return;
            }


            /** Is entrepreneur, last message is his. If accepted or rejected then set as seen answer */
            if (sessionUserId == m.getOwnerId() && !m.getDirection()) {
                if (m.getAccepted() != null)
                    m.setSeenAnswer();
                return;
            }

            m.setSeen();
        });

        return optionalMessage;
    }

    @Override
    public long getInvestedAmount(long sessionUserId, boolean investor) {
        MessageRequestBuilder request = new MessageRequestBuilder()
                .setAccepted();

        if (investor) request.setInvestor(sessionUserId);
        else request.setOwner(sessionUserId);

        List<Message> messageList = messageDao.findAll(request);
        return messageList.stream().map(m -> m.getContent().getOffer()).reduce(0L, Long::sum);
    }


    @Override
    public long projectNotifications(long projectId, long ownerId) {
        RequestBuilder request1 = new MessageRequestBuilder()
                .setOwner(ownerId)
                .setProject(projectId)
                .setUnseen()
                .setFromInvestor();

        RequestBuilder request2 = new MessageRequestBuilder()
                .setOwner(ownerId)
                .setProject(projectId)
                .setAnswered()
                .setUnseenAnswer()
                .setFromEntrepreneur();

        return messageDao.countAll(request1) + messageDao.countAll(request2);
    }


    @Override
    public long userNotifications(long sessionUserId, boolean isInvestor) {

        /** By default are messages from the first request are the one unseen */
        MessageRequestBuilder request1 = new MessageRequestBuilder()
                .setUnseen();

        /** By default are messages from the second request are the one with an answer and is unseen */
        MessageRequestBuilder request2 = new MessageRequestBuilder()
                .setAnswered()
                .setUnseenAnswer();

        /** If the user is an investor */
        if (isInvestor) {
            request1.setInvestor(sessionUserId)
                    .setFromEntrepreneur();

            request2.setInvestor(sessionUserId)
                    .setFromInvestor();

        /** If the user is an entrepreneur */
        } else {
            request1.setOwner(sessionUserId)
                    .setFromInvestor();

            request2.setOwner(sessionUserId)
                    .setFromEntrepreneur();
        }

        return messageDao.countAll(request1) + messageDao.countAll(request2);
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

        /** Messages accepted or rejected */
        if (message.getAccepted() != null)

            /** If it's accepted, then only the investor can start a new negotiation */
            if (message.getAccepted())
                return direction;

            /** Rejected the last message, both can send a new one */
            else return true;


        /** Messages that are not accepted or rejected */

        /** With an expiry date not yet crossed, sent only if not the last one */
        if (message.isExpiryDateValid())
            return message.getDirection() != direction;

        /** With an expiry date has expired, set the offer as rejected */
        message.setSeen();
        message.setSeenAnswer();
        message.setAccepted(false);
        return true;
    }

}