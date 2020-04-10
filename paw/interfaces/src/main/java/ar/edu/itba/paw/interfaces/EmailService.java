package ar.edu.itba.paw.interfaces;

public interface EmailService {

    void sendNewEmail(String to, String subject, String body);
}
