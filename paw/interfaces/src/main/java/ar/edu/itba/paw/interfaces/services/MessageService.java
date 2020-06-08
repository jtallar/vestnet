package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.model.Message;
import ar.edu.itba.paw.model.components.Page;


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
     * Identifies a message and updates its status.
     * @param senderId Unique user sender id.
     * @param receiverId Unique user receiver id.
     * @param projectId Unique project id.
     * @param accepted Status to be updated.
     * @return The updated message or null if not found.
     */
    Message updateMessageStatus(long senderId, long receiverId, long projectId, boolean accepted);
}
