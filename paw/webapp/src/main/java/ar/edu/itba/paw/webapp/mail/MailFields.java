package ar.edu.itba.paw.webapp.mail;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class MailFields {

   // @Size(min = 1, max = 140)
    private String body;

//    @Size(min = 1, max = 30)
//    private String subject;

   // @Size(min = 2, max = 20)
    //@Pattern(regexp = "[a-zA-Z0-9]+")    // chequeo q sea mail
    private String from;

    private String to;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

//    public String getSubject() {
//        return subject;
//    }
//
//    public void setSubject(String subject) {
//        this.subject = subject;
//    }

    @Override
    public String toString() {
        return "MailFields{" +
                "body='" + body + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                '}';
    }
}
