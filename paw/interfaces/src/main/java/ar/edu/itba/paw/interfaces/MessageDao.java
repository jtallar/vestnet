package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.Message;

import java.time.LocalDate;
import java.util.List;

public interface MessageDao {

    /**
     * Creates a new message.
     * @param message Message string.
     * @param offer Offer string.
     * @param interest Interest string.
     * @param senderId Unique user sender id.
     * @param receiverId Unique user receiver id.
     * @param projectId Unique project id.
     * @return operation return.
     */
    long create(String message, String offer, String interest, long senderId, long receiverId, long projectId) throws MessageAlreadySentException;

    /**
     * Gets all the messages from a negotiation.
     * @param entrepreneurId The unique user id.
     * @param investorId The unique investor id.
     * @param projectId The unique project id, which the conversation is about.
     * @return List of all the messages from the given conversation.
     */
    List<Message> getConversation(long entrepreneurId, long investorId, long projectId);

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

}
