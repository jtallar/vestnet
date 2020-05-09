package ar.edu.itba.paw.interfaces;

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
    void sendNewEmail(String from, String body, String offers, String exchange, String to) throws MessagingException;
}
