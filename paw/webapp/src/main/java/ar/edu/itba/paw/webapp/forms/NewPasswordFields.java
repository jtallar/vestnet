package ar.edu.itba.paw.webapp.forms;

import cz.jirutka.validator.spring.SpELAssert;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

@SpELAssert(value = "password.equals(repeatPassword)", message = "{ar.edu.itba.paw.webapp.forms.SpELAssert}")
public class NewPasswordFields {

    @Size(max = 50)
    @NotEmpty
    private String password;

    private String repeatPassword;

    private String email; // Inyected
    private String token; // Inyected

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public String getPassword() { return password;}

    public void setPassword(String password) { this.password = password; }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
