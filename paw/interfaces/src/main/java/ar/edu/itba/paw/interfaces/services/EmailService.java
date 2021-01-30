package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.model.Message;
import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.User;

import javax.mail.MessagingException;
import java.net.URI;

public interface EmailService {

    /**
     * Sends a formatted contact email.
     *
     * @param sender User mail sender.
     * @param receiver User recipient mail.
     * @param project Project name to contact about
     * @param content Content of the mail.
     * @param direction The direction of the message, true if from investor, false otherwise.
     * @param baseUri Base URL used to contact.
     * @throws MessagingException Upon sending error.
     */
    void sendOffer(User sender, User receiver, Project project, Message.MessageContent content, boolean direction, URI baseUri) throws MessagingException;


    /**
     * @param sender User mail sender.
     * @param receiver User mail receiver.
     * @param project Project id to reply about.
     * @param answer True if accepted, false if refused.
     * @param direction The direction of the message, true if from investor, false otherwise.
     * @param baseUri Base URI used to reply.
     * @throws MessagingException Upon sending error.
     */
    void sendOfferAnswer(User sender, User receiver, Project project, boolean answer, boolean direction, URI baseUri) throws MessagingException;


    /**
     * @param user User that requests new password
     * @param token Token used for URL
     * @param baseUri Base URI used to request new password.
     * @throws MessagingException Upon sending error.
     */
    void sendPasswordRecovery(User user, String token, URI baseUri) throws MessagingException;


    /**
     * Send a verification email
     *
     * @param user The user to be sent to.
     * @param token The token for verification.
     * @param baseUri The base url.
     * @throws MessagingException Upon sending error.
     */
    void sendVerification(User user, String token, URI baseUri) throws MessagingException;
}
