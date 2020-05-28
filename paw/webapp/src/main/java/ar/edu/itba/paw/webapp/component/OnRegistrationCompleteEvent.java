package ar.edu.itba.paw.webapp.component;

import org.springframework.context.ApplicationEvent;

import java.util.Locale;

public class OnRegistrationCompleteEvent extends ApplicationEvent {
    private String baseUrl;
    private Locale locale;
    private long user;

    public OnRegistrationCompleteEvent(long user, Locale locale, String baseUrl) {
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

    public long getUser() {
        return user;
    }
}
