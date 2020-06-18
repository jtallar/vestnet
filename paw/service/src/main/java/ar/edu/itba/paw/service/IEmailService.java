package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.services.EmailService;
import ar.edu.itba.paw.model.Mail;
import ar.edu.itba.paw.model.Message;
import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.User;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.Locale;

@Primary
@Service
public class IEmailService implements EmailService {

    private static final String VESTNET_EMAIL = "noreply.vestnet@gmail.com";

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private VelocityEngine velocityEngine;

    @Autowired
    private MessageSource messageSource;


    @Override //TODO aca en cada uno se tiene que setear el contenido del modelo MAIL, a mano algunas cosas, el contenido se hace con las funciones
    public void sendOffer(User sender, User receiver, Project project, Message.MessageContent content, String baseUrl) { // TODO de abajo. Luego se manda mail con el send mail nuevo.
        Locale localeInst = Locale.forLanguageTag(receiver.getLocale());

        String subject = messageSource.getMessage("email.subject.request", null, localeInst);

        String fullBodySB = messageSource.getMessage("email.body.vestnetReports", null, localeInst) + '\n' +
                MessageFormat.format(messageSource.getMessage("email.body.reportContact", null, localeInst),
                        String.format("%s %s", sender.getFirstName(), sender.getLastName()), project.getName()) +
                '\n' +
                messageSource.getMessage("email.body.messageHeader", null, localeInst) + "\n\n" +
                content.getMessage() + "\n\n" +
                MessageFormat.format(messageSource.getMessage("email.body.offer", null, localeInst), content.getOffer()) + '\n' +
                MessageFormat.format(messageSource.getMessage("email.body.inExchange", null, localeInst), content.getInterest()) + "\n\n" +
                MessageFormat.format(messageSource.getMessage("email.body.userProfile", null, localeInst),
                        String.format("%s/users/%d", baseUrl, sender.getId())) +
                "\n" +
                MessageFormat.format(messageSource.getMessage("email.body.contactInvestor", null, localeInst),
                        String.format("%s/dashboard#dashboard-project-%d", baseUrl, project.getId()));

        sendEmail(sender.getEmail(), receiver.getEmail(), subject, fullBodySB);

    }


    @Override //TODO aca en cada uno se tiene que setear el contenido del modelo MAIL, a mano algunas cosas, el contenido se hace con las funciones
    public void sendOfferAnswer(User sender, User receiver, Project project, boolean answer, String baseUrl) { // TODO de abajo. Luego se manda mail con el send mail nuevo.
        Locale localeInst = Locale.forLanguageTag(receiver.getLocale());

        String subject = messageSource.getMessage("email.subject.response", null, localeInst);

        String fullBodySB = messageSource.getMessage("email.body.vestnetReports", null, localeInst) + '\n' +
                ((answer) ? MessageFormat.format(messageSource.getMessage("email.body.acceptProposal", null, localeInst),
                        String.format("%s %s", sender.getFirstName(), sender.getLastName()), project.getName(),
                        String.format("%s/projects/%d", baseUrl, project.getId())) :
                        MessageFormat.format(messageSource.getMessage("email.body.rejectProposal", null, localeInst),
                                String.format("%s %s", sender.getFirstName(), sender.getLastName()), project.getName(),
                                String.format("%s/projects/%d", baseUrl, project.getId()))) +
                "\n\n" +
                MessageFormat.format(messageSource.getMessage("email.body.userProfile", null, localeInst),
                        String.format("%s/users/%d", baseUrl, sender.getId())) +
                "\n\n" +
                messageSource.getMessage("email.body.contactEntrepreneur", null, localeInst);
        sendEmail(sender.getEmail(), receiver.getEmail(), subject, fullBodySB);
    }


    @Override //TODO aca en cada uno se tiene que setear el contenido del modelo MAIL, a mano algunas cosas, el contenido se hace con las funciones
    public void sendPasswordRecovery(User user, String token, String baseUrl) {// TODO de abajo. Luego se manda mail con el send mail nuevo.
        Locale localeInst = Locale.forLanguageTag(user.getLocale());

        String subject = messageSource.getMessage("email.subject.passwordReset", null, localeInst);

        String fullBodySB = messageSource.getMessage("email.body.vestnetReports", null, localeInst) + '\n' +
                messageSource.getMessage("email.body.passwordReset", null, localeInst) + "\n" +
                MessageFormat.format(messageSource.getMessage("email.body.passwordResetButton", null, localeInst),
                        String.format("%s/resetPassword?token=%s", baseUrl, token));

        sendEmail(VESTNET_EMAIL, user.getEmail(), subject, fullBodySB);

    }


    @Override //TODO aca en cada uno se tiene que setear el contenido del modelo MAIL, a mano algunas cosas, el contenido se hace con las funciones
    public void sendVerification(User user, String token, String baseUrl) { // TODO de abajo. Luego se manda mail con el send mail nuevo.
        Locale localeInst = Locale.forLanguageTag(user.getLocale());

//        String subject = messageSource.getMessage("email.subject.verification", null, localeInst);
//
//        String fullBodySB = messageSource.getMessage("email.body.vestnetReports", null, localeInst) + '\n' +
//                messageSource.getMessage("email.body.verification", null, localeInst) + "\n" +
//                MessageFormat.format(messageSource.getMessage("email.body.verificationButton", null, localeInst),
//                        String.format("%s/verify?token=%s", baseUrl, token));
//
//        sendEmail(VESTNET_EMAIL, user.getEmail(), subject, fullBodySB);
        Mail mail = new Mail();
        mail.setFrom(VESTNET_EMAIL);
        mail.setTo(user.getEmail());
        mail.setSubject(messageSource.getMessage("email.subject.verification", null, localeInst));
        mail.setContent(prepareVerificationEmail(mail, user, token, baseUrl));
        sendEmail(mail);
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


    /** Velocity new auxiliary functions */


    /**
     * Sends a formatted mail in html.
     * @param mail The formatted mail. Content must be filled previously.
     */
    public void sendEmail(Mail mail) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setSubject(mail.getSubject());
            mimeMessageHelper.setFrom(mail.getFrom());
            mimeMessageHelper.setTo(mail.getTo());
            mimeMessageHelper.setText(mail.getContent(), true);

            emailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


    /**
     * Prepares the body formatting for a new offer mail.
     * @param mail The mail contents, without the content per se.
     * @param sender The user sender.
     * @param receiver The user receiver.
     * @param project The project in matter.
     * @param offer The offer content.
     * @param baseUrl The base url for the answer.
     * @return Formatted text in html.
     */
    private String prepareOfferEmail(Mail mail, User sender, User receiver, Project project, Message.MessageContent offer, String baseUrl) {
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("mail", mail);
        velocityContext.put("sender", sender);
        velocityContext.put("receiver", receiver);
        velocityContext.put("project", project);
        velocityContext.put("offer", offer);
        velocityContext.put("baseUrl", baseUrl);
        StringWriter stringWriter = new StringWriter();
        velocityEngine.mergeTemplate("/templates/offer.vm", "UTF-8", velocityContext, stringWriter);
        return stringWriter.toString();
    }


    /**
     * Prepares the body formatting for an offer answer mail.
     * @param mail The mail contents, without the content per se.
     * @param sender The user sender.
     * @param receiver The user receiver.
     * @param project The project in matter.
     * @param answer The answer of the made offer.
     * @param baseUrl The base url for the answer.
     * @return Formatted text in html.
     */
    private String prepareOfferAnswerEmail(Mail mail, User sender, User receiver, Project project, boolean answer, String baseUrl) {
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("mail", mail);
        velocityContext.put("sender", sender);
        velocityContext.put("receiver", receiver);
        velocityContext.put("project", project);
        velocityContext.put("answer", answer);
        velocityContext.put("baseUrl", baseUrl);
        StringWriter stringWriter = new StringWriter();
        velocityEngine.mergeTemplate("/templates/offer-answer.vm", "UTF-8", velocityContext, stringWriter);
        return stringWriter.toString();
    }


    /**
     * Prepares the body formatting for a password recovery mail.
     * @param mail The mail contents, without the content per se.
     * @param user The user receiver model.
     * @param token The generated token.
     * @param baseUrl The base url for the answer.
     * @return Formatted text in html.
     */
    private String preparePasswordRecoveryEmail(Mail mail, User user, String token, String baseUrl) {
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("mail", mail);
        velocityContext.put("user", user);
        velocityContext.put("token", token);
        velocityContext.put("baseUrl", baseUrl);
        StringWriter stringWriter = new StringWriter();
        velocityEngine.mergeTemplate("/templates/password-recovery.vm", "UTF-8", velocityContext, stringWriter);
        return stringWriter.toString();
    }


    /**
     * Prepares the body formatting for a user verification mail.
     * @param mail The mail contents, without the content per se.
     * @param user The user receiver model.
     * @param token The generated token.
     * @param baseUrl The base url for the answer.
     * @return Formatted text in html.
     */
    private String prepareVerificationEmail(Mail mail, User user, String token, String baseUrl) {
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("mail", mail);
        velocityContext.put("user", user);
        velocityContext.put("token", token);
        velocityContext.put("baseUrl", baseUrl);
        velocityContext.put("messages", this.messageSource);
        velocityContext.put("locale", Locale.forLanguageTag(user.getLocale()));
        StringWriter stringWriter = new StringWriter();
        velocityEngine.mergeTemplate("/templates/user-verification.vm", "UTF-8", velocityContext, stringWriter);
        return stringWriter.toString();
    }

}