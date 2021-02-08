package ar.edu.itba.paw.webapp.dto.user;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class MailDto {

    @NotEmpty
    @Email
    private String mail;

    /* Getters and setters */

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
