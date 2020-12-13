package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.model.Message;
import ar.edu.itba.paw.model.components.Page;

import java.net.URI;
import java.util.List;
import java.util.Optional;


public interface MessageService {

    /**
     * Creates a new message.
     * @param messageData The data for the new message to create.
     * @param sessionUserId The session user id that sent the message.
     * @param baseUri Base url for replies.
     * @return The optional created message. Empty in case of errors.
     */
    Optional<Message> create(Message messageData, long sessionUserId, URI baseUri);


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
     * @param page The page to return.
     * @param pageSize The page size (amount of messages from different investors).
     * @return Paged messages. Empty if owner is not the real owner of the project.
     */
    Page<Message> getProjectInvestors(long projectId, long ownerId, int page, int pageSize);


    /**
     * Gets the last message for each different project inverted by the investor.
     * @param investorId The investor ID of the request.
     * @param page The page to return.
     * @param pageSize The page size (amount of messages from different projects).
     * @return Paged messages.
     */
    Page<Message> getInvestorProjects(long investorId, int page, int pageSize);


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

    // Previously user service

//    /**
//     * Gets all the user accepted offers.
//     * @param receiverId User in matter unique id.
//     * @param page Page requested.
//     * @param pageSize Page size.
//     * @return Messages page with the required data.
//     */
//    Page<Message> getAcceptedMessages(long receiverId, int page, int pageSize);
//
//
//    /**
//     * Gets all the user made offers.
//     * @param senderId User in matter unique id.
//     * @param page Page requested.
//     * @param pageSize Page size.
//     * @return Messages page with the required data.
//     */
//    Page<Message> getOfferMessages(long senderId, int page, int pageSize);
//
//
//    /**
//     * Gets all the unread messages for an user and a project.
//     * @param userId Unique user id.
//     * @param projectId Unique project id.
//     * @return A list of all the messages that match criteria.
//     */
//    List<Message> getProjectUnreadMessages(long userId, long projectId);
//
//
//    /**
//     * Gets the last sent message.
//     * @param userId Unique user id.
//     * @param projectId Unique project id.
//     * @return An optional for the last sent message.
//     */
//    Optional<Message> getLastProjectOfferMessage(long userId, long projectId);

}
