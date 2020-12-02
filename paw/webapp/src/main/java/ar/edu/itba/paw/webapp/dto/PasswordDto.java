package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.forms.validators.EqualFields;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

@EqualFields({"password", "repeatPassword"})
public class PasswordDto {
    @Size(max = 50)
    @NotEmpty
    private String password;

    private String repeatPassword;

    // TODO: Ver si recibimos por aca tambien el token

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }
}
