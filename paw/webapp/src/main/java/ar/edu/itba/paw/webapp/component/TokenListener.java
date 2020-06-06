package ar.edu.itba.paw.webapp.component;

import ar.edu.itba.paw.interfaces.services.EmailService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class TokenListener implements ApplicationListener<SendTokenEvent> {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Override
    public void onApplicationEvent(SendTokenEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(SendTokenEvent event) {
        User user = event.getUser();
        String token = userService.createToken(user.getId()).getToken();
        switch (event.getEventType()) {
            case FORGOT_PASSWORD: emailService.sendPasswordRecovery(user, token, event.getBaseUrl()); break;
            case USER_VERIFICATION: emailService.sendVerification(user, token, event.getBaseUrl()); break;
        }
    }
}
