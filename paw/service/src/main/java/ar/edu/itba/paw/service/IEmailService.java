package ar.edu.itba.paw.service;

//import ar.edu.itba.paw.interfaces.services.EmailService;
import ar.edu.itba.paw.interfaces.services.EmailService;
import ar.edu.itba.paw.model.Message;
import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Locale;

@Primary
@Service
public class IEmailService implements EmailService {

    private static final String VESTNET_EMAIL = "noreply.vestnet@gmail.com";

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private MessageSource messageSource;


    @Override
    public void sendOffer(User sender, User receiver, Project project, Message.MessageContent content, String baseUrl) {
        Locale localeInst = Locale.forLanguageTag(receiver.getLocale());

        String subject = messageSource.getMessage("email.subject.request", null, localeInst);

        String fullBodySB = messageSource.getMessage("email.body.vestnetReports", null, localeInst) + '\n' +
                MessageFormat.format(messageSource.getMessage("email.body.reportContact", null, localeInst),
                        String.format("%s %s", sender.getFirstName(), sender.getLastName()), project) +
                '\n' +
                messageSource.getMessage("email.body.messageHeader", null, localeInst) + "\n\n" +
                content.getMessage() + "\n\n" +
                MessageFormat.format(messageSource.getMessage("email.body.offer", null, localeInst), content.getOffer()) + '\n' +
                MessageFormat.format(messageSource.getMessage("email.body.inExchange", null, localeInst), content.getInterest()) + "\n\n" +
                MessageFormat.format(messageSource.getMessage("email.body.userProfile", null, localeInst),
                        String.format("%s/users/%d", baseUrl, sender.getId())) +
                "\n" +
                MessageFormat.format(messageSource.getMessage("email.body.contactInvestor", null, localeInst),
                        String.format("%s/messages#dashboard-project-%d", baseUrl, project.getId()));

        sendEmail(sender.getEmail(), receiver.getEmail(), subject, fullBodySB);

    }


    @Override
    public void sendOfferAnswer(User sender, User receiver, Project project, boolean answer, String baseUrl) {
        Locale localeInst = Locale.forLanguageTag(receiver.getLocale());

        String subject = messageSource.getMessage("email.subject.response", null, localeInst);

        String fullBodySB = messageSource.getMessage("email.body.vestnetReports", null, localeInst) + '\n' +
                ((answer) ? MessageFormat.format(messageSource.getMessage("email.body.acceptProposal", null, localeInst),
                        String.format("%s %s", sender.getFirstName(), sender.getLastName()),
                        String.format("%s/projects/%d", baseUrl, project.getId())) :
                        MessageFormat.format(messageSource.getMessage("email.body.rejectProposal", null, localeInst),
                                String.format("%s %s", sender.getFirstName(), sender.getLastName()),
                                String.format("%s/projects/%d", baseUrl, project.getId()))) +
                "\n\n" +
                MessageFormat.format(messageSource.getMessage("email.body.userProfile", null, localeInst),
                        String.format("%s/users/%d", baseUrl, sender.getId())) +
                "\n\n" +
                messageSource.getMessage("email.body.contactEntrepreneur", null, localeInst);
        sendEmail(sender.getEmail(), receiver.getEmail(), subject, fullBodySB);
    }


    @Override
    public void sendPasswordRecovery(User user, String token, String baseUrl) {
        Locale localeInst = Locale.forLanguageTag(user.getLocale());

        String subject = messageSource.getMessage("email.subject.passwordReset", null, localeInst);

        String fullBodySB = messageSource.getMessage("email.body.vestnetReports", null, localeInst) + '\n' +
                messageSource.getMessage("email.body.passwordReset", null, localeInst) + "\n" +
                MessageFormat.format(messageSource.getMessage("email.body.passwordResetButton", null, localeInst),
                        String.format("%s/resetPassword?token=%s", baseUrl, token));

        sendEmail(VESTNET_EMAIL, user.getEmail(), subject, fullBodySB);

    }


    @Override
    public void sendVerification(User user, String token, String baseUrl) {
        Locale localeInst = Locale.forLanguageTag(user.getLocale());

        String subject = messageSource.getMessage("email.subject.verification", null, localeInst);

        String fullBodySB = messageSource.getMessage("email.body.vestnetReports", null, localeInst) + '\n' +
                messageSource.getMessage("email.body.verification", null, localeInst) + "\n" +
                MessageFormat.format(messageSource.getMessage("email.body.verificationButton", null, localeInst),
                        String.format("%s/verify?token=%s", baseUrl, token));

        sendEmail(VESTNET_EMAIL, user.getEmail(), subject, fullBodySB);

    }


    /** Auxiliary functions */

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