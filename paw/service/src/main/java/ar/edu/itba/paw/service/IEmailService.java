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
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.StringWriter;
import java.util.Locale;

@Primary
@Service
public class IEmailService implements EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private VelocityEngine velocityEngine;

    @Autowired
    private MessageSource messageSource;


    @Override
    public void sendOffer(User sender, User receiver, Project project, Message.MessageContent content, String baseUrl) {
        Mail mail = new Mail();
        mail.setFrom(sender.getEmail());
        mail.setTo(receiver.getEmail());
        mail.setSubject(messageSource.getMessage("email.subject.request", null, Locale.forLanguageTag(receiver.getLocale())));
        mail.setContent(prepareOfferEmail(mail, sender, receiver, project, content, baseUrl));
        sendEmail(mail);

    }


    @Override
    public void sendOfferAnswer(User sender, User receiver, Project project, boolean answer, String baseUrl) {
        Mail mail = new Mail();
        mail.setFrom(sender.getEmail());
        mail.setTo(receiver.getEmail());
        mail.setSubject(messageSource.getMessage("email.subject.response", null, Locale.forLanguageTag(receiver.getLocale())));
        mail.setContent(prepareOfferAnswerEmail(mail, sender, receiver, project, answer, baseUrl));
        sendEmail(mail);
    }


    @Override
    public void sendPasswordRecovery(User user, String token, String baseUrl) {
        Mail mail = new Mail();
        mail.setTo(user.getEmail());
        mail.setSubject(messageSource.getMessage("email.subject.passwordReset", null, Locale.forLanguageTag(user.getLocale())));
        mail.setContent(prepareTokenMail(mail, user, token, baseUrl, true));
        sendEmail(mail);
    }


    @Override
    public void sendVerification(User user, String token, String baseUrl) {
        Mail mail = new Mail();
        mail.setTo(user.getEmail());
        mail.setSubject(messageSource.getMessage("email.subject.verification", null, Locale.forLanguageTag(user.getLocale())));
        mail.setContent(prepareTokenMail(mail, user, token, baseUrl, true));
        sendEmail(mail);
    }


    /** Auxiliary functions */


    /**
     * Sends a formatted mail in html.
     * @param mail The formatted mail. Content must be filled previously.
     */
    public void sendEmail(Mail mail) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            mimeMessageHelper.setSubject(mail.getSubject());
            mimeMessageHelper.setFrom(mail.getFrom());
            mimeMessageHelper.setTo(mail.getTo());
            mimeMessageHelper.setText(mail.getContent(), true);
            ClassPathResource resource = new ClassPathResource("/images/mail-header.png");
            mimeMessageHelper.addInline("headerImage", resource);

            emailSender.send(mimeMessageHelper.getMimeMessage());

        } catch (MessagingException e) {
//            e.printStackTrace(); // TODO should we do something? Retry send?
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
        VelocityContext velocityContext = defaultContextInit(mail, receiver, baseUrl);

        velocityContext.put("sender", sender);
        velocityContext.put("project", project);
        velocityContext.put("offer", offer);

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
        VelocityContext velocityContext = defaultContextInit(mail, receiver, baseUrl);

        velocityContext.put("sender", sender);
        velocityContext.put("project", project);
        velocityContext.put("answer", answer);

        StringWriter stringWriter = new StringWriter();
        velocityEngine.mergeTemplate("/templates/offer-answer.vm", "UTF-8", velocityContext, stringWriter);
        return stringWriter.toString();
    }


    /**
     * Prepares the body formatting for a user verification and password recovery mail.
     * @param mail The mail contents, without the content per se.
     * @param receiver The user receiver model.
     * @param token The generated token.
     * @param baseUrl The base url for the answer.
     * @param verification If the mail is verification or password recovery.
     * @return Formatted text in html.
     */
    private String prepareTokenMail(Mail mail, User receiver, String token, String baseUrl, boolean verification) {

        VelocityContext velocityContext = defaultContextInit(mail, receiver, baseUrl);
        velocityContext.put("token", token);

        StringWriter stringWriter = new StringWriter();
        String template = "/templates/user-verification.vm";
        if (!verification) template = "/templates/password-recovery.vm";
        velocityEngine.mergeTemplate(template, "UTF-8", velocityContext, stringWriter);
        return stringWriter.toString();
    }


    /**
     * Creates a default context initialization used by all methods.
     * @param mail The mail contents, without the content per se.
     * @param receiver User receiver.
     * @param baseUrl The base url for the answer.
     * @return The initialized velocity context.
     */
    private VelocityContext defaultContextInit(Mail mail, User receiver, String baseUrl) {
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("mail", mail);
        velocityContext.put("receiver", receiver);
        velocityContext.put("baseUrl", baseUrl);
        velocityContext.put("messages", this.messageSource);
        velocityContext.put("locale", Locale.forLanguageTag(receiver.getLocale()));
//        velocityContext.put("header", new )
        return velocityContext;
    }

}