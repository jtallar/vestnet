package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.daos.MessageDao;
import ar.edu.itba.paw.interfaces.exceptions.InvalidMessageException;
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
    public Message create(long projectId, long investorId, long sessionUserId, Message.MessageContent content, int expiryDays,
                          URI baseUri) throws InvalidMessageException {

        /** Checks for the existence of the project and the owner ID is the right one */
        final Project project = projectService.findById(projectId).orElseThrow(InvalidMessageException::new);

        /** Checks it the session user id is from one of the two users negotiating, and sets direction of message */
        final boolean direction;
        if (sessionUserId == investorId)
            direction = true;
        else if (sessionUserId == project.getOwnerId())
            direction = false;
        else throw new InvalidMessageException("Session user is not part of the message.");

        /** Checks if both users exists */
        final User owner = userService.findById(project.getOwnerId()).orElseThrow(InvalidMessageException::new);
        final User investor = userService.findById(investorId).orElseThrow(InvalidMessageException::new);

        /** Checks if the user is able to sent message */
        isPostOfferValid(owner.getId(), investorId, projectId, direction);

        /** Creates the message data to persist */
        final Message messageData = new Message(content, new User(project.getOwnerId()),
                new User(investorId), new Project(projectId), direction, expiryDays);

        /** Sends email */
        emailService.sendOffer(owner, investor, project, messageData.getContent(), messageData.getDirection(), baseUri);

        /** Persists message */
        return messageDao.create(messageData);
    }


    @Override
    public Optional<Message> getLastChatMessage(long projectId, long investorId, long sessionUserId) {
        final RequestBuilder request = new MessageRequestBuilder()
                .setProject(projectId)
                .setInvestor(investorId)
                .setOwner(sessionUserId, (sessionUserId != investorId))
                .setOrder(OrderField.DATE_DESCENDING);

        return messageDao.findAll(request).stream().findFirst();
    }

    @Override
    public Page<Message> getProjectInvestors(long projectId, long ownerId, boolean accepted, int page, int pageSize) {
        final MessageRequestBuilder request = new MessageRequestBuilder()
                .setOwner(ownerId)
                .setProject(projectId)
                .setOrder(OrderField.DATE_DESCENDING);

        if (accepted) request.setAccepted();
        else request.setGroup(GroupField.INVESTOR);
        return messageDao.findAll(request, new PageRequest(page, pageSize));
    }


    @Override
    public Page<Message> getInvestorProjects(long investorId, boolean accepted, int page, int pageSize) {
        final MessageRequestBuilder request = new MessageRequestBuilder()
                .setInvestor(investorId)
                .setOrder(OrderField.DATE_DESCENDING);

        if (accepted) request.setAccepted();
        else request.setGroup(GroupField.PROJECT);

        return messageDao.findAll(request, new PageRequest(page, pageSize));
    }


    @Override
    public Page<Message> getConversation(long projectId, long investorId, long sessionUserId, int page, int pageSize) {
        final RequestBuilder request = new MessageRequestBuilder()
                .setProject(projectId)
                .setInvestor(investorId)
                /** If session user is the investor then don't need to check project ownership */
                .setOwner(sessionUserId, (sessionUserId != investorId))
                .setOrder(OrderField.DATE_DESCENDING);

        return messageDao.findAll(request, new PageRequest(page, pageSize));
    }


    @Override
    @Transactional
    public Optional<Message> updateMessageStatus(long projectId, long investorId, long sessionUserId, boolean accepted,
                                                 URI baseUri) throws InvalidMessageException {
        final Optional<Message> optionalMessage = getLastChatMessage(projectId, investorId, sessionUserId);

        if (!optionalMessage.isPresent()) return Optional.empty();
        final Message message = optionalMessage.get();

        /** This cannot happen as the message if its on the database, then the owner, investor and project exist */
        final Project project = projectService.findById(projectId).orElseThrow(InvalidMessageException::new);
        final User owner = userService.findById(message.getOwnerId()).orElseThrow(InvalidMessageException::new);
        final User investor = userService.findById(investorId).orElseThrow(InvalidMessageException::new);

        /** If the message has expired */
        if (!message.isExpiryDateValid()) {
            message.setAccepted(false);
            throw new InvalidMessageException("Cannot answer over an expired message.");
        }

        /** Valid expire date */
        /** Is investor, last message cannot be his */
        if (sessionUserId == investorId && message.getDirection())
            throw new InvalidMessageException("Cannot answer over an own message.");

        /** Is entrepreneur, last message cannot be his */
        if (sessionUserId == message.getOwnerId() && !message.getDirection())
            throw new InvalidMessageException("Cannot answer over an own message.");

        /** Send email */
        emailService.sendOfferAnswer(owner, investor, project, accepted, message.getDirection(), baseUri);

        /** Set message as accepted or not, and if accepted add the new funds */
        message.setAccepted(accepted);
        if (accepted)
            project.setFundingCurrent(project.getFundingCurrent() + message.getContent().getOffer());

        return optionalMessage;
    }


    @Override
    @Transactional
    public Optional<Message> updateMessageSeen(long projectId, long investorId, long sessionUserId, URI baseUri) {
        final Optional<Message> optionalMessage = getLastChatMessage(projectId, investorId, sessionUserId);
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
        final MessageRequestBuilder request = new MessageRequestBuilder()
                .setAccepted();

        if (investor) request.setInvestor(sessionUserId);
        else request.setOwner(sessionUserId);

        final List<Message> messageList = messageDao.findAll(request);
        return messageList.stream().map(m -> m.getContent().getOffer()).reduce(0L, Long::sum);
    }


    @Override
    public long projectNotifications(long projectId, long ownerId) {
        final RequestBuilder request1 = new MessageRequestBuilder()
                .setOwner(ownerId)
                .setProject(projectId)
                .setUnseen()
                .setFromInvestor();

        final RequestBuilder request2 = new MessageRequestBuilder()
                .setOwner(ownerId)
                .setProject(projectId)
                .setAnswered()
                .setUnseenAnswer()
                .setFromEntrepreneur();

        return messageDao.countAll(request1, request2);
    }


    @Override
    public long userNotifications(long sessionUserId, boolean isInvestor) {

        /** By default are messages from the first request are the one unseen */
        final MessageRequestBuilder request1 = new MessageRequestBuilder()
                .setUnseen();

        /** By default are messages from the second request are the one with an answer and is unseen */
        final MessageRequestBuilder request2 = new MessageRequestBuilder()
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

        return messageDao.countAll(request1, request2);
    }



    /** Auxiliary Methods */

    /**
     * Checks if the user is able to make the post offer message or not. Based on the last previous message.
     * @param ownerId The owner's unique ID.
     * @param investorId The investor's unique ID.
     * @param projectId The project's unique ID.
     * @param direction The direction of the conversation given. True for investor to entrepreneur. False otherwise.
     * @throws InvalidMessageException If the message is not valid to be sent.
     */
    private void isPostOfferValid(long ownerId, long investorId, long projectId, boolean direction) throws InvalidMessageException {
        final RequestBuilder request = new MessageRequestBuilder()
                .setOwner(ownerId)
                .setInvestor(investorId)
                .setProject(projectId)
                .setOrder(OrderField.DATE_DESCENDING);

        final Optional<Message> lastMessage = messageDao.findAll(request).stream().findFirst();

        /** Opening of a new negotiation, only the investors  */
        if (!lastMessage.isPresent())
            if (direction) return;
            else throw new InvalidMessageException("New negotiation cannot be opened by entrepreneur.");

        final Message message = lastMessage.get();

        /** Middle of negotiation */

        /** Messages accepted or rejected */
        if (message.getAccepted() != null)

            /** If it's accepted, then only the investor can start a new negotiation */
            if (message.getAccepted())
                if (direction) return;
                else throw new InvalidMessageException("New offer after an accepted one cannot be made by entrepreneur");

            /** Rejected the last message, both can send a new one */
            else return;


        /** Messages that are not accepted or rejected */

        /** With an expiry date not yet crossed, sent only if not the last one */
        if (message.isExpiryDateValid())
            if (message.getDirection() != direction) return;
            else throw new InvalidMessageException("Cannot send new offer before the expiry date of a own previous offer.");

        /** With an expiry date has expired, set the offer as rejected */
        message.setSeen();
        message.setSeenAnswer();
        message.setAccepted(false);
    }
}