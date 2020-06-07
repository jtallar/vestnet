package ar.edu.itba.paw.webapp.event;

import ar.edu.itba.paw.model.Message;
import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.User;
import org.springframework.context.ApplicationEvent;

public class OfferAnswerEvent extends ApplicationEvent {
    private User sender;
    private User receiver;
    private Project project;
    private boolean answer;
    private String baseUrl;

    public OfferAnswerEvent(User sender, User receiver, Project project, boolean answer, String baseUrl) {
        super(sender);
        this.baseUrl = baseUrl;
        this.sender = sender;
        this.receiver = receiver;
        this.project = project;
        this.answer = answer;
    }

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public Project getProject() {
        return project;
    }

    public boolean getAnswer() {
        return answer;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}
