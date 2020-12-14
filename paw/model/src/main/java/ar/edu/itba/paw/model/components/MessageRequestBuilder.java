package ar.edu.itba.paw.model.components;

import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.User;

import static ar.edu.itba.paw.model.components.FilterField.*;

/**
 * Creates the list of criteria filter and order for
 * the messages requests.
 */
public class MessageRequestBuilder extends RequestBuilder {

    public MessageRequestBuilder() {
        super();
    }


    /**
     * Sets to filter the owner.
     * @param id The receiver's user id.
     * @return The RequestBuilder
     */
    public MessageRequestBuilder setOwner(long id) {
        criteriaList.add(new FilterCriteria(MESSAGE_ENTREPRENEUR, new User(id)));
        return this;
    }


    /**
     * Sets to filter the owner if the condition is true.
     * @param id The receiver's user id.
     * @return The RequestBuilder
     */
    public MessageRequestBuilder setOwner(long id, boolean condition) {
        if (condition) criteriaList.add(new FilterCriteria(MESSAGE_ENTREPRENEUR, new User(id)));
        return this;
    }


    /**
     * Sets to filter the investor.
     * @param id The sender's user id.
     * @return The RequestBuilder
     */
    public MessageRequestBuilder setInvestor(long id) {
        criteriaList.add(new FilterCriteria(MESSAGE_INVESTOR, new User(id)));
        return this;
    }


    /**
     * Sets to filter the project.
     * @param id The project's id.
     * @return The RequestBuilder
     */
    public MessageRequestBuilder setProject(long id) {
        criteriaList.add(new FilterCriteria(MESSAGE_PROJECT, new Project(id)));
        return this;
    }


    /**
     * Sets to filter the unread messages.
     * @return The RequestBuilder
     */
    public MessageRequestBuilder setSeen() {
        criteriaList.add(new FilterCriteria(MESSAGE_SEEN, true));
        return this;
    }


    /**
     * Sets to filter the accepted messages.
     * @return The RequestBuilder
     */
    public MessageRequestBuilder setAccepted() {
        criteriaList.add(new FilterCriteria(MESSAGE_ACCEPTED, true));
        return this;
    }


    /**
     * Sets to order the request.
     * @param order Value of the order, if not valid, default order is set.
     * @return The RequestBuilder.
     */
    public MessageRequestBuilder setOrder(int order) {
        this.order = OrderField.getEnum(String.valueOf(order));
        return this;
    }


    /**
     * Sets to order the request.
     * @param order OrderField to set.
     * @return The RequestBuilder.
     */
    public MessageRequestBuilder setOrder(OrderField order) {
        this.order = order;
        return this;
    }


    /**
     * Sets the group by clause.
     * @param group GroupField to set.
     * @return The RequestBuilder.
     */
    public MessageRequestBuilder setGroup(GroupField group) {
        this.group = group;
        return this;
    }

}
