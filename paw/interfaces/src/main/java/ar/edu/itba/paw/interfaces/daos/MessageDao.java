package ar.edu.itba.paw.interfaces.daos;

import ar.edu.itba.paw.interfaces.exceptions.MessageAlreadySentException;
import ar.edu.itba.paw.model.Message;
import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.components.Page;

import java.util.List;

public interface MessageDao {

    /**
     * Creates a new message.
     * @param content Message content. Message, offer, interest.
     * @param sender User sender.
     * @param receiver User receiver.
     * @param project Project message topic.
     * @return The created message.
     */
    Message create(Message.MessageContent content, User sender, User receiver, Project project) throws MessageAlreadySentException;














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

    Page<Message> getAccepted(long receiver_id, long from, long to);

    Integer countAccepted(long receiver_id);


    List<Message> getOffersDone(long sender_id, long from, long to);

    Integer countOffers(long sender_id);


}
