package ar.edu.itba.paw.model.components;

import ar.edu.itba.paw.model.Message;
import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.User;

public class MessageRequestBuilder extends RequestBuilder {

    public MessageRequestBuilder() {
        super();
    }

    public MessageRequestBuilder setReceiver(long id) {
        criteriaList.add(new FilterCriteria("receiver", new User(id)));
        return this;
    }

    public MessageRequestBuilder setSender(long id) {
        criteriaList.add(new FilterCriteria("sender", new User(id)));
        return this;
    }

    public MessageRequestBuilder setProject(long id) {
        criteriaList.add(new FilterCriteria("project", new Project(id)));
        return this;
    }

    public MessageRequestBuilder setUnread(boolean state) {
        criteriaList.add(new FilterCriteria("unread", state));
        return this;
    }

    public MessageRequestBuilder setAccepted(boolean state) {
        criteriaList.add(new FilterCriteria("accepted", state));
        return this;
    }

    public MessageRequestBuilder setOrder(int order) {
        this.order = OrderField.getEnum(String.valueOf(order));
        return this;
    }

    public MessageRequestBuilder setOrder(OrderField order) {
        this.order = order;
        return this;
    }

}
