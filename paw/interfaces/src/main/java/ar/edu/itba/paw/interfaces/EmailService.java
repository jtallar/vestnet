package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.User;

import javax.mail.MessagingException;

public interface EmailService {

    /**
     * Sends a formatted contact email.
     *
     * @param sender    User mail sender.
     * @param body      Content of the mail.
     * @param offers    Offer of the mail.
     * @param exchange  Exchange of the mail.
     * @param to        User recipient mail.
     * @param project   Project name to contact about
     * @param projectId Project id to contact about
     * @param baseUrl   Base URL used to contact
     * @param locale    Receiver Locale
     * @throws MessagingException When mail cannot be sent.
     */
    void sendNewEmail(User sender, String body, int offers, String exchange, String to, String project, long projectId, String baseUrl, String locale) throws MessagingException;


    /**
     * @param sender    User mail sender.
     * @param answer    True if accepted, false if refused
     * @param to        User recipient mail.
     * @param projectId Project id to reply about
     * @param baseUrl   Base URL used to reply
     * @param locale    Receiver Locale
     * @throws MessagingException When mail cannot be sent.
     */
    void sendEmailAnswer(User sender, boolean answer, String to, long projectId, String baseUrl, String locale) throws MessagingException;

    /**
     * @param user    User that requests new password
     * @param token   Token used for URL
     * @param baseUrl Base URL used to request new password
     * @throws MessagingException When mail cannot be sent.
     */
    void sendPasswordRecovery(User user, String token, String baseUrl) throws MessagingException;

    /**
     * Send a verification email
     *
     * @param user    The user to be sent to.
     * @param token   The token for verification.
     * @param baseUrl The base url
     * @throws MessagingException
     */
    void sendVerificationEmail(User user, String token, String baseUrl) throws MessagingException;
}
