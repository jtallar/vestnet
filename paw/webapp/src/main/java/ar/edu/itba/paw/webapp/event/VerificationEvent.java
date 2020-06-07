package ar.edu.itba.paw.webapp.event;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.components.TokenEventType;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

public class VerificationEvent extends ApplicationEvent {
    private String baseUrl;
    private User user;

    public VerificationEvent(User user, String baseUrl) {
        super(user);

        this.user = user;
        this.baseUrl = baseUrl;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public User getUser() {
        return user;
    }
}
