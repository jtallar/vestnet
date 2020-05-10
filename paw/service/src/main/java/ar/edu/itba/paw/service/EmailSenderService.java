package ar.edu.itba.paw.service;

import  ar.edu.itba.paw.interfaces.EmailService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Primary
@Service
public class EmailSenderService implements EmailService {

    @Override
    public void sendNewEmail(String from, String body, int offers, String exchange, String to, String link) throws MessagingException {

        Properties props = new Properties();

        // Nombre del host de correo, es smtp.gmail.com
        props.setProperty("mail.smtp.host", "smtp.gmail.com");
        // TLS si está disponible
        props.setProperty("mail.smtp.starttls.enable", "true");
        // Puerto de gmail para envio de correos
        props.setProperty("mail.smtp.port", "587");
        // Nombre del usuario
        props.setProperty("mail.smtp.user", "noreply.vestnet@gmail.com");
        // Si requiere o no usuario y password para conectarse.
        props.setProperty("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        session.setDebug(true);

        MimeMessage message = new MimeMessage(session);

        StringBuilder fullBodySB = new StringBuilder();
        // fullBodySB.append("VestNet informa:\n" + "El usuario " + from + " desea comunicarse contigo.\nEl mensaje es el siguiente:\n\n" + body + "\n\nRecuerde que este es un ");
        fullBodySB.append("VestNet reports:\n" + "User ").append(from).append(" wants to contact you.\nThe message is as follows:\n\n").append("Offer: ")
                .append(offers).append("\nExchange: ").append(exchange).append("\n\n").append(body).append("\n\nFor visiting user's VestNet Profile enter here: ")
                .append(link).append("\n\nTo contact him, reply this email normally.");

        String fullBody = fullBodySB.toString();

        try {
            // Quien envia el correo
            message.setFrom(new InternetAddress("noreply.vestnet@gmail.com"));
            // A quien va dirigido
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("VestNet - Potential Investor");
            message.setText(fullBody);
            message.setReplyTo(new javax.mail.Address[]
                    {
                            new javax.mail.internet.InternetAddress(from)
                    });

            Transport t = session.getTransport("smtp");
            t.connect("noreply.vestnet@gmail.com","VN123456");
            t.sendMessage(message,message.getAllRecipients());
            t.close();

        } catch (MessagingException me) {
            me.printStackTrace();   //Si se produce un error
            System.out.println("ERROR AL ENVIAR EMAIL");
            throw new MessagingException();
        }
    }
    @Override
    public void sendEmailAnswer(String from, boolean answer, String to, String link) throws MessagingException {

        Properties props = new Properties();

        // Nombre del host de correo, es smtp.gmail.com
        props.setProperty("mail.smtp.host", "smtp.gmail.com");
        // TLS si está disponible
        props.setProperty("mail.smtp.starttls.enable", "true");
        // Puerto de gmail para envio de correos
        props.setProperty("mail.smtp.port", "587");
        // Nombre del usuario
        props.setProperty("mail.smtp.user", "noreply.vestnet@gmail.com");
        // Si requiere o no usuario y password para conectarse.
        props.setProperty("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        session.setDebug(true);

        MimeMessage message = new MimeMessage(session);

        StringBuilder fullBodySB = new StringBuilder();
        // fullBodySB.append("VestNet informa:\n" + "El usuario " + from + " desea comunicarse contigo.\nEl mensaje es el siguiente:\n\n" + body + "\n\nRecuerde que este es un ");

        String body;
        if(answer)
            body = " has accepted your purpose!";
        else
            body = " has rejected your purpose.";

        fullBodySB.append("VestNet reports:\n" + "User ").append(from).append(body)
                .append("\n\nFor visiting User's VestNet Profile enter here: ").append(link).append("\n\nTo contact him, reply this email normally.");
        String fullBody = fullBodySB.toString();

        try {
            // Quien envia el correo
            message.setFrom(new InternetAddress("noreply.vestnet@gmail.com"));
            // A quien va dirigido
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("VestNet - Potential Investor");
            message.setText(fullBody);
            message.setReplyTo(new javax.mail.Address[]
                    {
                            new javax.mail.internet.InternetAddress(from)
                    });

            Transport t = session.getTransport("smtp");
            t.connect("noreply.vestnet@gmail.com","VN123456");
            t.sendMessage(message,message.getAllRecipients());
            t.close();

        } catch (MessagingException me) {
            me.printStackTrace();   //Si se produce un error
            System.out.println("ERROR AL ENVIAR EMAIL");
            throw new MessagingException();
        }
    }
}