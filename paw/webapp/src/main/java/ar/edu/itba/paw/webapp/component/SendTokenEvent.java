package ar.edu.itba.paw.webapp.component;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.components.TokenEventType;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

public class SendTokenEvent extends ApplicationEvent {
    private String baseUrl;
    private User user;
    private TokenEventType eventType;

    public SendTokenEvent(User user, String baseUrl, TokenEventType eventType) {
        super(user);

        this.user = user;
        this.baseUrl = baseUrl;
        this.eventType = eventType;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public User getUser() {
        return user;
    }

    public TokenEventType getEventType() {
        return eventType;
    }
}
