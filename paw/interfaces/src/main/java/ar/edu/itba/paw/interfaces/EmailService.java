package ar.edu.itba.paw.interfaces;

import javax.mail.MessagingException;

public interface EmailService {

    void sendNewEmail(String from, String body, String offers, String exchange, String to) throws MessagingException;
}
