package ar.edu.itba.paw.webapp.forms;

import ar.edu.itba.paw.webapp.config.WebConfig;
import ar.edu.itba.paw.webapp.forms.validators.ImageFile;
import cz.jirutka.validator.spring.SpELAssert;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@SpELAssert(value = "password.equals(repeatPassword)", message = "{ar.edu.itba.paw.webapp.forms.SpELAssert}")
public class NewUserFields {

    @Size(max = 50)
    @NotEmpty
    private String password;
    private String repeatPassword;

    @Size(max = 25)
    @NotEmpty
    private String firstName;

    @Size(max = 25)
    @NotEmpty
    private String lastName;

    @NotEmpty
    private String realId; // CUIT/CUIL/DNI

    private Integer day;

    private Integer month;

    private Integer year;

    private Integer country;

    private Integer state;

    private Integer city;

    private String role;

    @NotEmpty
    @Pattern(regexp="^([^@]+@[^@]+\\.[a-zA-Z]{2,}$)?")
    private String email;

    @Pattern(regexp = "[0-9]*")
    private String phone;

    @Pattern(regexp = "^((http(s)?://)?(www\\.)?(linkedin\\.com/in/)([-a-zA-Z0-9@:%_+.~#?&=/]*)$)?")
    private String linkedin;

    @ImageFile(maxSize = WebConfig.MAX_UPLOAD_SIZE)
    private MultipartFile profilePicture;


    /** Getters and setters */

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRealId() {
        return realId;
    }

    public void setRealId(String realId) {
        this.realId = realId;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getCountry() {
        return country;
    }

    public void setCountry(Integer country) {
        this.country = country;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getCity() {
        return city;
    }

    public void setCity(Integer city) {
        this.city = city;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public MultipartFile getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(MultipartFile profilePicture) {
        this.profilePicture = profilePicture;
    }

    @Override
    public String toString() {
        return "NewUserFields{" +
                "password='" + password + '\'' +
                ", repeatPassword='" + repeatPassword + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", realId='" + realId + '\'' +
                ", day=" + day +
                ", month=" + month +
                ", year=" + year +
                ", country=" + country +
                ", state=" + state +
                ", city=" + city +
                ", role='" + role + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", linkedin='" + linkedin + '\'' +
                ", profilePicture=" + profilePicture +
                '}';
    }
}
