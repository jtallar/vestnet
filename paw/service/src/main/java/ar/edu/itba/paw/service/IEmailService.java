package ar.edu.itba.paw.service;

//import ar.edu.itba.paw.interfaces.services.EmailService;
import ar.edu.itba.paw.interfaces.services.EmailService;
import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Base64;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Primary
@Service
public class IEmailService implements EmailService {

    private static final String VESTNET_EMAIL = "noreply.vestnet@gmail.com";

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private MessageSource messageSource;

    @Override
    public void sendNewEmail(User sender, String body, int offers, String exchange, String to, String project,
                             long projectId, String baseUrl, String locale) {

        Locale localeInst = Locale.forLanguageTag(locale);

        String subject = messageSource.getMessage("email.subject.request", null, localeInst);

        String fullBodySB = messageSource.getMessage("email.body.vestnetReports", null, localeInst) + '\n' +
                MessageFormat.format(messageSource.getMessage("email.body.reportContact", null, localeInst),
                        String.format("%s %s", sender.getFirstName(), sender.getLastName()), project) +
                '\n' +
                messageSource.getMessage("email.body.messageHeader", null, localeInst) + "\n\n" +
                body + "\n\n" +
                MessageFormat.format(messageSource.getMessage("email.body.offer", null, localeInst), offers) + '\n' +
                MessageFormat.format(messageSource.getMessage("email.body.inExchange", null, localeInst), exchange) + "\n\n" +
                MessageFormat.format(messageSource.getMessage("email.body.userProfile", null, localeInst),
                        String.format("%s/users/%d", baseUrl, sender.getId())) +
                "\n" +
                MessageFormat.format(messageSource.getMessage("email.body.contactInvestor", null, localeInst),
                        String.format("%s/messages#dashboard-project-%d", baseUrl, projectId));

        sendEmail(sender.getEmail(), to, subject, fullBodySB);

    }

    @Override
    public void sendEmailAnswer(User sender, boolean answer, String to, long projectId, String baseUrl, String locale) {

        Locale localeInst = Locale.forLanguageTag(locale);

        String subject = messageSource.getMessage("email.subject.response", null, localeInst);

        String fullBodySB = messageSource.getMessage("email.body.vestnetReports", null, localeInst) + '\n' +
                ((answer) ? MessageFormat.format(messageSource.getMessage("email.body.acceptProposal", null, localeInst),
                        String.format("%s %s", sender.getFirstName(), sender.getLastName()),
                        String.format("%s/projects/%d", baseUrl, projectId)) :
                        MessageFormat.format(messageSource.getMessage("email.body.rejectProposal", null, localeInst),
                                String.format("%s %s", sender.getFirstName(), sender.getLastName()),
                                String.format("%s/projects/%d", baseUrl, projectId))) +
                "\n\n" +
                MessageFormat.format(messageSource.getMessage("email.body.userProfile", null, localeInst),
                        String.format("%s/users/%d", baseUrl, sender.getId())) +
                "\n\n" +
                messageSource.getMessage("email.body.contactEntrepreneur", null, localeInst);
        sendEmail(sender.getEmail(), to, subject, fullBodySB);
    }

    @Override
    public void sendPasswordRecovery(User user, String token, String baseUrl) {
        // TODO eventually fix this
        Locale localeInst = Locale.forLanguageTag(/*user.getLocation().getCountry().getLocale()*/"en");

        String subject = messageSource.getMessage("email.subject.passwordReset", null, localeInst);

        String fullBodySB = messageSource.getMessage("email.body.vestnetReports", null, localeInst) + '\n' +
                messageSource.getMessage("email.body.passwordReset", null, localeInst) + "\n" +
                MessageFormat.format(messageSource.getMessage("email.body.passwordResetButton", null, localeInst),
                        String.format("%s/resetPassword?token=%s", baseUrl, token));

        sendEmail(VESTNET_EMAIL, user.getEmail(), subject, fullBodySB);

    }

    @Override
    public void sendVerification(User user, String token, String baseUrl) {
        // TODO eventually fix also this
        Locale localeInst = Locale.forLanguageTag(/*user.getLocation().getCountry().getLocale()*/"en");

        String subject = messageSource.getMessage("email.subject.verification", null, localeInst);

        String fullBodySB = messageSource.getMessage("email.body.vestnetReports", null, localeInst) + '\n' +
                messageSource.getMessage("email.body.verification", null, localeInst) + "\n" +
                MessageFormat.format(messageSource.getMessage("email.body.verificationButton", null, localeInst),
                        String.format("%s/verify?token=%s", baseUrl, token));

        sendEmail(VESTNET_EMAIL, user.getEmail(), subject, fullBodySB);

    }


    /**
     * Sends a simple mail message.
     * @param from Where to reply to.
     * @param to Mail to reach to.
     * @param subject Subject of the mail.
     * @param body Mail body.
     */
    private void sendEmail(String from, String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setReplyTo(from);
        emailSender.send(message);
    }
}