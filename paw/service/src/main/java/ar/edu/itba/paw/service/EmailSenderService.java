package ar.edu.itba.paw.service;

import  ar.edu.itba.paw.interfaces.EmailService;
import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Primary
@Service
public class EmailSenderService implements EmailService {

    private static final String VESTNET_EMAIL = "noreply.vestnet@gmail.com";

    @Override
    public void sendNewEmail(User sender, String body, int offers, String exchange, String to, String project, long projectId, String baseUrl) throws MessagingException {

        Properties props = getEmailProperties();
        Session session = Session.getDefaultInstance(props);
        session.setDebug(true);

        // TODO: Cambiar locale al que le mande yo --> NO ES DEL sender, FALTA PARAM
        Locale locale = new Locale("es");
        ResourceBundle bundle = ResourceBundle.getBundle("i18n/emailMessages", locale);

        String subject = bundle.getString("email.subject.request");
        StringBuilder fullBodySB = new StringBuilder();
        fullBodySB.append(bundle.getString("email.body.vestnetReports")).append('\n')
                .append(MessageFormat.format(bundle.getString("email.body.reportContact"),
                        String.format("%s %s", sender.getFirstName(), sender.getLastName()), project)).append('\n')
                .append(bundle.getString("email.body.messageHeader")).append("\n\n")
                .append(body).append("\n\n")
                .append(MessageFormat.format(bundle.getString("email.body.offer"), offers)).append('\n')
                .append(MessageFormat.format(bundle.getString("email.body.inExchange"), exchange)).append("\n\n")
                .append(MessageFormat.format(bundle.getString("email.body.userProfile"),
                        String.format("%s/users/%d", baseUrl, sender.getId()))).append("\n")
                .append(MessageFormat.format(bundle.getString("email.body.contactInvestor"),
                        String.format("%s/messages#dashboard-project-%d", baseUrl, projectId)));

        try {
            sendEmail(session, sender.getEmail(), to, subject, fullBodySB.toString());
        } catch (MessagingException me) {
            me.printStackTrace();   //Si se produce un error
            System.out.println("ERROR AL ENVIAR EMAIL");
            throw new MessagingException();
        }
    }
    @Override
    public void sendEmailAnswer(User sender, boolean answer, String to, long projectId, String baseUrl) throws MessagingException {

        Properties props = getEmailProperties();
        Session session = Session.getDefaultInstance(props);
        session.setDebug(true);

        // TODO: Cambiar locale al que le mande yo
        Locale locale = new Locale("en-US");
        ResourceBundle bundle = ResourceBundle.getBundle("i18n/emailMessages", locale);

        String subject = bundle.getString("email.subject.response");
        StringBuilder fullBodySB = new StringBuilder();
        fullBodySB.append(bundle.getString("email.body.vestnetReports")).append('\n')
                .append((answer) ? MessageFormat.format(bundle.getString("email.body.acceptProposal"),
                        String.format("%s %s", sender.getFirstName(), sender.getLastName()),
                        String.format("%s/projects/%d", baseUrl, projectId)) :
                        MessageFormat.format(bundle.getString("email.body.rejectProposal"),
                                String.format("%s %s", sender.getFirstName(), sender.getLastName()),
                                String.format("%s/projects/%d", baseUrl, projectId))).append("\n\n")
                .append(MessageFormat.format(bundle.getString("email.body.userProfile"),
                        String.format("%s/users/%d", baseUrl, sender.getId()))).append("\n\n")
                .append(bundle.getString("email.body.contactEntrepreneur"));

        try {
            sendEmail(session, sender.getEmail(), to, subject, fullBodySB.toString());
        } catch (MessagingException me) {
            me.printStackTrace();   //Si se produce un error
            System.out.println("ERROR AL ENVIAR EMAIL");
            throw new MessagingException();
        }
    }

    private Properties getEmailProperties() {
        Properties props = new Properties();

        // Nombre del host de correo, es smtp.gmail.com
        props.setProperty("mail.smtp.host", "smtp.gmail.com");
        // TLS si está disponible
        props.setProperty("mail.smtp.starttls.enable", "true");
        // Puerto de gmail para envio de correos
        props.setProperty("mail.smtp.port", "587");
        // Nombre del usuario
        props.setProperty("mail.smtp.user", VESTNET_EMAIL);
        // Si requiere o no usuario y password para conectarse.
        props.setProperty("mail.smtp.auth", "true");

        return props;
    }

    private void sendEmail(Session session, String from, String to, String subject, String body) throws MessagingException {
        MimeMessage message = new MimeMessage(session);
        // Quien envia el correo
        message.setFrom(new InternetAddress(VESTNET_EMAIL));
        // A quien va dirigido
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);
        message.setText(body);
        message.setReplyTo(new javax.mail.Address[]
                {
                        new javax.mail.internet.InternetAddress(from)
                });

        Transport t = session.getTransport("smtp");
        t.connect(VESTNET_EMAIL,"VN123456");
        t.sendMessage(message,message.getAllRecipients());
        t.close();
    }
}