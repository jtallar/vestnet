package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.User;

import javax.mail.MessagingException;

public interface EmailService {

    /**
     * Sends a formatted email.
     * @param from User mail sender.
     * @param body Content of the mail.
     * @param offers Offer of the mail.
     * @param exchange Exchange of the mail.
     * @param to User recipient mail.
     * @throws MessagingException When mail cannot be sent.
     */
    void sendNewEmail(User user, String body, int offers, String exchange, String to, String project, long projectId, String baseUrl, String locale) throws MessagingException;

    void sendEmailAnswer(User sender, boolean answer, String to, long projectId, String baseUrl, String locale) throws MessagingException;
}
