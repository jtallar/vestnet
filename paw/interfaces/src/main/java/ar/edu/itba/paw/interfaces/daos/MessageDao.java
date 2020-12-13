package ar.edu.itba.paw.interfaces.daos;

import ar.edu.itba.paw.model.Message;
import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.components.*;

import java.util.List;

public interface MessageDao {

    /**
     * Creates a new message.
     * @param content Message content. Comment, offer, interest.
     * @param owner User owner of the project.
     * @param investor User investor interested on project.
     * @param project Project message topic.
     * @param direction If true then is from the investor, if false from the entrepreneur.
     * @return The created message.
     */
    Message create(Message.MessageContent content, User owner, User investor, Project project, boolean direction);


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
}
