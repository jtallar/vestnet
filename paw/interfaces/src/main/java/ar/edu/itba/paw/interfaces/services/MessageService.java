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
     * Identifies a message and updates its status.
     * @param senderId Unique user sender id.
     * @param receiverId Unique user receiver id.
     * @param projectId Unique project id.
     * @param accepted Status to be updated.
     * @param baseUri Base uri for replies.
     * @return The updated message or null if not found.
     */
    Optional<Message> updateMessageStatus(long senderId, long receiverId, long projectId, boolean accepted, URI baseUri);

    /**
     * Gets the last message for each investor for the project.
     * @param projectId The project unique ID to bring all the last conversations.
     * @param ownerId The owner ID of the request.
     * @param page The page to return.
     * @param pageSize The page size (amount of messages from different investors).
     * @return Paged messages. Empty if owner is not the real owner of the project.
     */
    Page<Message> getProjectInvestors(long projectId, long ownerId, int page, int pageSize);


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
