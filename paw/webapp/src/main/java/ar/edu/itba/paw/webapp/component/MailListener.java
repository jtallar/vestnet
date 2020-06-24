package ar.edu.itba.paw.webapp.component;

import ar.edu.itba.paw.interfaces.services.EmailService;
import ar.edu.itba.paw.interfaces.services.MessageService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.event.OfferAnswerEvent;
import ar.edu.itba.paw.webapp.event.OfferEvent;
import ar.edu.itba.paw.webapp.event.PasswordRecoveryEvent;
import ar.edu.itba.paw.webapp.event.VerificationEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class MailListener {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Async
    @EventListener
    public void verification(VerificationEvent event) {
        User user = event.getUser();
        String token = userService.createToken(user.getId()).getToken();
        emailService.sendVerification(user, token, event.getBaseUrl());
    }

    @Async
    @EventListener
    public void passwordRecovery(PasswordRecoveryEvent event) {
        User user = event.getUser();
        String token = userService.createToken(user.getId()).getToken();
        emailService.sendPasswordRecovery(user, token, event.getBaseUrl());
    }

    @Async
    @EventListener
    public void offer(OfferEvent event) {
        emailService.sendOffer(event.getSender(), event.getReceiver(), event.getProject(), event.getMessage().getContent(), event.getBaseUrl());
    }

    @Async
    @EventListener
    public void offerAnswer(OfferAnswerEvent event) {
        emailService.sendOfferAnswer(event.getSender(), event.getReceiver(), event.getProject(), event.getAnswer(), event.getBaseUrl());
    }
}
