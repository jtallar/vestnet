package ar.edu.itba.paw.webapp.forms;

import ar.edu.itba.paw.model.Location;
import cz.jirutka.validator.spring.SpELAssert;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

//TODO cannot print this message
@SpELAssert(value = "password.equals(repeatPassword)")
public class NewUserFields {

    private String password; //TODO> add password to DAO
    private String repeatPassword;
    @Size(min = 1, max = 50)
    @NotEmpty
    private String firstName;
    @Size(min = 1, max = 50)
    @NotEmpty
    private String lastName;

    @NotEmpty
    private String realId; // CUIT/CUIL/DNI
    private Integer day;
    private Integer month;
    private Integer year;


// Aca si quiero que se complete despues, debiera poner un locationId que sea final y un Location que no lo sea
    @Email
    @NotEmpty
    @Pattern(regexp="^[^@]+@[^@]+\\.[a-zA-Z]{2,}$")
    private String email;
    @Pattern(regexp = "[0-9]*")
    private String phone;
    @Pattern(regexp = "^(.*(www\\.)?(linkedin\\.com/in/).*)?")
    private String linkedin;
    private String profilePicture; //if we need URI then change it later





    public Integer getDay() { return day; }

    public void setDay(Integer day) { this.day = day; }

    public Integer getMonth() { return month; }

    public void setMonth(Integer month) { this.month = month; }

    public Integer getYear() { return year; }

    public void setYear(Integer year) { this.year = year; }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public void setFirstName(String firstName) {this.firstName = firstName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public void setRealId(String realId) { this.realId = realId; }



    public void setEmail(String email) { this.email = email; }

    public void setPhone(String phone) { this.phone = phone; }

    public void setLinkedin(String linkedin) { this.linkedin = linkedin; }


    public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }

    public String getPassword() { return password;}

    public void setPassword(String password) { this.password = password; }


    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public String getRealId() { return realId; }



    public String getEmail() { return email; }

    public String getPhone() { return phone; }

    public String getLinkedin() { return linkedin; }


    public String getProfilePicture() { return profilePicture; }
}
