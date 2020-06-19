package ar.edu.itba.paw.model;

import java.util.Date;
import java.util.List;

public class Mail {

    private static final String DEFAULT_EMAIL = "noreply.vestnet@gmail.com";

    private String from;

    private String to;

    private String cc;

    private String bcc;

    private String subject;

    private String content;

    private String contentType;

    private List<Object> attachments;

    public Mail() {
        contentType = "text/plain";
        from = DEFAULT_EMAIL;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Date getMailSendDate() {
        return new Date();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Object> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Object> attachments) {
        this.attachments = attachments;
    }


    @Override
    public String toString() {
        return "Mail{" +
                "mailFrom='" + from + '\'' +
                ", mailTo='" + to + '\'' +
                ", mailCc='" + cc + '\'' +
                ", mailBcc='" + bcc + '\'' +
                ", mailSubject='" + subject + '\'' +
                ", mailContent='" + content + '\'' +
                ", contentType='" + contentType + '\'' +
                ", attachments=" + attachments +
                '}';
    }
}
