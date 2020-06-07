package ar.edu.itba.paw.webapp.event;

import ar.edu.itba.paw.model.Message;
import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.User;
import org.springframework.context.ApplicationEvent;

/** Event for a new contact offer */
public class OfferEvent extends ApplicationEvent {
    private User sender;
    private User receiver;
    private Project project;
    private Message.MessageContent content;
    private String baseUrl;

    public OfferEvent(User sender, User receiver, Project project, String body, int offer, String interest, String baseUrl) {
        super(sender);
        this.baseUrl = baseUrl;
        this.sender = sender;
        this.receiver = receiver;
        this.project = project;
        this.content = new Message.MessageContent(body, String.valueOf(offer), interest);
    }


    /** Getters */

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public Project getProject() {
        return project;
    }

    public Message.MessageContent getContent() {
        return content;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}
