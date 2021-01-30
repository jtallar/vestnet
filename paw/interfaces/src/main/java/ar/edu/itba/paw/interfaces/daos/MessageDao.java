package ar.edu.itba.paw.interfaces.daos;

import ar.edu.itba.paw.model.Message;
import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.components.*;

import java.util.List;

public interface MessageDao {

    /**
     * Creates a new message.
     * @param messageData The new message data ready to be commited.
     * @return The created message.
     */
    Message create(Message messageData);


    /**
     * Finds all the messages given the filters, ordered and paged.
     * @param request Builder with filters and order.
     * @param page Page request.
     * @return Page with the messages and pagination data.
     */
    Page<Message> findAll(RequestBuilder request, PageRequest page);


    /**
     * Finds all the messages given the filters, ordered not paged.
     * @param request Filters and order to be applied to the messages.
     * @return List with the messages.
     */
    List<Message> findAll(RequestBuilder request);


    /**
     * Gets the count of messages that matches the criteria.
     * Usually used for notification messages.
     * @param request Filters and order to be applied to the messages.
     * @return The amount of messages matching the criteria.
     */
    long countAll(RequestBuilder request);


    /**
     * Gets the count of messages that matches the criteria.
     * Usually used for notification messages. Used as request1 OR request2.
     * @param request1 Filters and order to be applied to the messages.
     * @param request2 Filters and order to be applied to the messages.
     * @return The amount of messages matching the criteria.
     */
    long countAll(RequestBuilder request1, RequestBuilder request2);
}
