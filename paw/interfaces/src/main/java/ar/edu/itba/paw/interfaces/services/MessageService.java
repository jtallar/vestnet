package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.model.Message;
import ar.edu.itba.paw.model.components.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface MessageService {

    /**
     * Creates a new message.
     * @param message Message string.
     * @param offer Offer string.
     * @param interest Interest string.
     * @param senderId Unique user sender id.
     * @param receiverId Unique user receiver id.
     * @param projectId Unique project id.
     * @return The created message.
     */
    Message create(String message, int offer, String interest, long senderId, long receiverId, long projectId);


    /**
     * Gets all the user accepted offers.
     * @param receiverId User in matter unique id.
     * @param page Page requested.
     * @param pageSize Page size.
     * @return Messages page with the required data.
     */
    Page<Message> getUserAccepted(long receiverId, Integer page, Integer pageSize);


    /**
     * Gets all the user made offers.
     * @param senderId User in matter unique id.
     * @param page Page requested.
     * @param pageSize Page size.
     * @return Messages page with the required data.
     */
    Page<Message> getUserOffers(long senderId, Integer page, Integer pageSize);


    /**
     * Gets an entire conversation paged.
     * @param receiverId User receiver id.
     * @param senderId User sender id.
     * @param projectId Project in matter.
     * @param page Page requested.
     * @param pageSize Page size.
     * @return Messages page with the required data.
     */
    Page<Message> getConversation(long receiverId, long senderId, long projectId, Integer page, Integer pageSize);


    /**
     * Gets all the unread messages for an user and a project.
     * @param userId Unique user id.
     * @param projectId Unique project id.
     * @return A list of all the messages that match criteria.
     */
    List<Message> getUserProjectUnread(long userId, long projectId);


    /**
     * Gets the last sent message.
     * @param userId Unique user id.
     * @param projectId Unique project id.
     * @return An optional for the last sent message.
     */
    Optional<Message> getUserProjectLast(long userId, long projectId);


    /**
     * Identifies a message and updates its status.
     * @param senderId Unique user sender id.
     * @param receiverId Unique user receiver id.
     * @param projectId Unique project id.
     * @param accepted Status to be updated.
     * @return The updated message or null if not found.
     */
    Message updateMessageStatus(long senderId, long receiverId, long projectId, boolean accepted);
}
