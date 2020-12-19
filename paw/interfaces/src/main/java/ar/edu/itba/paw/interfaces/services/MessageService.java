package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.model.Message;
import ar.edu.itba.paw.model.Message.MessageContent;
import ar.edu.itba.paw.model.components.Page;

import java.net.URI;
import java.util.List;
import java.util.Optional;


public interface MessageService {

    /**
     * Creates a new message.
     * @param projectId The project's id on which the offer is being made.
     * @param investorId The investor id making or receiving the offer.
     * @param sessionUserId The session user id that sent the message.
     * @param content The data for the new message to create.
     * @param expiryDays The amount of days until the offer expires.
     * @param baseUri Base url for replies.
     * @return The optional created message. Empty in case of errors.
     */
    Optional<Message> create(long projectId, long investorId, long sessionUserId, MessageContent content, int expiryDays, URI baseUri);


    /**
     * Gets the last message from a specific chat, used for the status change.
     * @param projectId The unique project ID from which is the conversation about.
     * @param investorId The unique investor's ID.
     * @param sessionUserId The session user ID to check valid requests.
     * @return The optional of the message. Empty if there is none or is a bad request.
     */
    Optional<Message> getLastChatMessage(long projectId, long investorId, long sessionUserId);


    /**
     * Gets the last message for each investor for the project.
     * @param projectId The project unique ID to bring all the last conversations.
     * @param ownerId The owner ID of the request.
     * @param accepted True to bring all the accepted transactions.
     * @param page The page to return.
     * @param pageSize The page size (amount of messages from different investors).
     * @return Paged messages. Empty if owner is not the real owner of the project.
     */
    Page<Message> getProjectInvestors(long projectId, long ownerId, boolean accepted, int page, int pageSize);


    /**
     * Gets the last message for each different project inverted by the investor.
     * @param investorId The investor ID of the request.
     * @param accepted True to bring all the accepted transactions.
     * @param page The page to return.
     * @param pageSize The page size (amount of messages from different projects).
     * @return Paged messages.
     */
    Page<Message> getInvestorProjects(long investorId, boolean accepted, int page, int pageSize);


    /**
     * Gets all the messages of the conversation of an project with an investor.
     * @param projectId The unique project ID from which is the conversation about.
     * @param investorId The unique investor's ID.
     * @param sessionUserId The session user ID to check valid requests.
     * @param page The page to return.
     * @param pageSize The page size (the n amount of last messages).
     * @return Paged messages.
     */
    Page<Message> getConversation(long projectId, long investorId, long sessionUserId, int page, int pageSize);


    /**
     * Identifies a message and updates its accepted/rejected status.
     * @param projectId Unique project id.
     * @param investorId The investor's unique ID.
     * @param sessionUserId The session user ID.
     * @param accepted Status to be updated.
     * @param baseUri Base uri for replies.
     * @return The updated message or null if not found.
     */
    Optional<Message> updateMessageStatus(long projectId, long investorId, long sessionUserId, boolean accepted, URI baseUri);


    /**
     * Identifies a message and updates its seen status.
     * @param projectId Unique project id.
     * @param investorId The investor's unique ID.
     * @param sessionUserId The session user ID.
     * @param baseUri Base uri for replies.
     * @return The updated message or null if not found.
     */
    Optional<Message> updateMessageSeen(long projectId, long investorId, long sessionUserId, URI baseUri);


    /**
     * Returns the amount of unread chats for this specific project.
     * @param projectId The specific project's unique id.
     * @param ownerId The owner's unique id.
     * @return The amount of unread chats.
     */
    long projectNotifications(long projectId, long ownerId);


    /**
     * Returns the total amount of unread chats for a specific user.
     * @param sessionUserId The user to fetch amount of notifications.
     * @param isInvestor True if is investor, false otherwise.
     * @return The amount of unread chats.
     */
    long userNotifications(long sessionUserId, boolean isInvestor);

}
