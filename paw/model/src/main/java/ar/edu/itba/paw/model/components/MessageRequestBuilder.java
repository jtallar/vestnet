package ar.edu.itba.paw.model.components;

import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.User;

/**
 * Creates the list of criteria filter and order for
 * the messages requests.
 */
public class MessageRequestBuilder extends RequestBuilder {

    public MessageRequestBuilder() {
        super();
    }


    /**
     * Sets to filter the receiver.
     * @param id The receiver's user id.
     * @return The RequestBuilder
     */
    public MessageRequestBuilder setReceiver(long id) {
        criteriaList.add(new FilterCriteria("receiver", new User(id)));
        return this;
    }


    /**
     * Sets to filter the sender.
     * @param id The sender's user id.
     * @return The RequestBuilder
     */
    public MessageRequestBuilder setSender(long id) {
        criteriaList.add(new FilterCriteria("sender", new User(id)));
        return this;
    }


    /**
     * Sets to filter the project.
     * @param id The project's id.
     * @return The RequestBuilder
     */
    public MessageRequestBuilder setProject(long id) {
        criteriaList.add(new FilterCriteria("project", new Project(id)));
        return this;
    }


    /**
     * Sets to filter the unread messages.
     * @param state The read state of the messages.
     * @return The RequestBuilder
     */
    public MessageRequestBuilder setUnread(boolean state) {
        criteriaList.add(new FilterCriteria("unread", state));
        return this;
    }


    /**
     * Sets to filter the accepted messages.
     * @param state The accepted state of the messages.
     * @return The RequestBuilder
     */
    public MessageRequestBuilder setAccepted(boolean state) {
        criteriaList.add(new FilterCriteria("accepted", state));
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

}
