package ar.edu.itba.paw.interfaces.daos;

import ar.edu.itba.paw.interfaces.exceptions.MessageAlreadySentException;
import ar.edu.itba.paw.model.Message;
import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.components.FilterCriteria;
import ar.edu.itba.paw.model.components.OrderField;
import ar.edu.itba.paw.model.components.Page;
import ar.edu.itba.paw.model.components.PageRequest;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    Message create(Message.MessageContent content, User sender, User receiver, Project project) throws MessageAlreadySentException;


    /**
     * Finds all the messages given the filters, ordered and paged.
     * @param filters Filters to be applied to the messages.
     * @param order Order to bring the messages.
     * @param page Page request.
     * @return Page with the messages and pagination data.
     */
    Page<Message> findAll(List<FilterCriteria> filters, OrderField order, PageRequest page);


    /**
     * Finds all the messages given the filters, ordered not paged.
     * @param filters Filters to be applied to the messages.
     * @param order Order to bring the messages.
     * @return List with the messages.
     */
    List<Message> findAll(List<FilterCriteria> filters, OrderField order);


    /**
     * Identifies a message and updates its status.
     * @param filters Filters needed to identify the message.
     * @param accepted Status to be updated.
     * @return The updated message on null if not found.
     */
    @Transactional
    Message updateMessageStatus(List<FilterCriteria> filters, boolean accepted);
}
