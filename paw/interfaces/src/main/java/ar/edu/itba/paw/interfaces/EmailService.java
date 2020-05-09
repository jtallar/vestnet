package ar.edu.itba.paw.interfaces;

import javax.mail.MessagingException;

public interface EmailService {

    /**
     * Sends a email with the right formatting.
     * @param from Mail of the sender.
     * @param body Content of the mail.
     * @param to Mail of the recipient
     * @throws MessagingException When the email cannot be sent.
     */
    void sendNewEmail(String from, String body, String to) throws MessagingException;
}
