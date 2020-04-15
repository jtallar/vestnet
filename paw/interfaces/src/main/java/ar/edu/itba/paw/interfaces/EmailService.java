package ar.edu.itba.paw.interfaces;

public interface EmailService {

    void sendNewEmail(String from, String body, String to);
}
