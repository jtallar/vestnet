package ar.edu.itba.paw.webapp.event;

import ar.edu.itba.paw.model.User;
import org.springframework.context.ApplicationEvent;

/** Event for password recovery */
public class PasswordRecoveryEvent extends ApplicationEvent {
    private String baseUrl;
    private User user;

    public PasswordRecoveryEvent(User user, String baseUrl) {
        super(user);

        this.user = user;
        this.baseUrl = baseUrl;
    }


    /** Getters */

    public String getBaseUrl() {
        return baseUrl;
    }

    public User getUser() {
        return user;
    }

}
