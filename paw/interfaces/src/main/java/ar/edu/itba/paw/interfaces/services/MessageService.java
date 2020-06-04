package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.interfaces.exceptions.MessageAlreadySentException;
import ar.edu.itba.paw.model.Message;
import ar.edu.itba.paw.model.components.Page;

import java.util.List;

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
    Message create(String message, int offer, String interest, long senderId, long receiverId, long projectId) throws MessageAlreadySentException;


    /**
     * Gets accepted messages for a receiver.
     * @param receiver_id The user receiver id.
     * @param page The page to ask.
     * @param pageSize The page size.
     * @return Page with the messages.
     */
    Page<Message> getAccepted(long receiver_id, Integer page, Integer pageSize);







    /**
     * Gets all unread messages from a specific project.
     * @param userId The user id requesting its unread messages.
     * @param projectId The project id.
     * @return List of all the messages to the user of the given project.
     */
    List<Message> getProjectUnread(long userId, long projectId);

    /**
     * Identifies a message and updates its status.
     * @param senderId Unique user sender id.
     * @param receiverId Unique user receiver id.
     * @param projectId Unique project id.
     * @param accepted Status to be updated.
     * @return operation return.
     */
    long updateMessageStatus(long senderId, long receiverId, long projectId, boolean accepted);








    Integer countAccepted( long receiver_id);


    List<Message> getOffersDone(long sender_id, String page, long to);

    Integer countOffers(long sender_id);


    Boolean hasNextRequest(String page, long id);

    Boolean hasNextDeal(String page, long id);

    Integer getPageSize();

}
