package ar.edu.itba.paw.webapp.component;

import ar.edu.itba.paw.model.User;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

public class OnRegistrationCompleteEvent extends ApplicationEvent {
    private String baseUrl;
    private Locale locale;
    private User user;

    public OnRegistrationCompleteEvent(User user, Locale locale, String baseUrl) {
        super(user);

        this.user = user;
        this.locale = locale;
        this.baseUrl = baseUrl;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public Locale getLocale() {
        return locale;

    }

    public User getUser() {
        return user;
    }
}
