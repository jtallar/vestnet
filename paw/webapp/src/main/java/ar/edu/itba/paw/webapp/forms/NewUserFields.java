package ar.edu.itba.paw.webapp.forms;

import ar.edu.itba.paw.model.Location;

import java.util.Date;

public class NewUserFields {


    private String password; //TODO> add password to DAO
    private String repeatPassword;
    private String firstName;
    private String lastName;
    private String realId; // CUIT/CUIL/DNI
    private Date birthDate;
    // Aca si quiero que se complete despues, debiera poner un locationId que sea final y un Location que no lo sea

    private String email;
    private String phone;
    private String linkedin;
    private String profilePicture; //if we need URI then change it later

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public void setFirstName(String firstName) {this.firstName = firstName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public void setRealId(String realId) { this.realId = realId; }

    public void setBirthDate(Date birthDate) { this.birthDate = birthDate; }


    public void setEmail(String email) { this.email = email; }

    public void setPhone(String phone) { this.phone = phone; }

    public void setLinkedin(String linkedin) { this.linkedin = linkedin; }


    public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }

    public String getPassword() { return password;}

    public void setPassword(String password) { this.password = password; }


    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public String getRealId() { return realId; }

    public Date getBirthDate() { return birthDate; }


    public String getEmail() { return email; }

    public String getPhone() { return phone; }

    public String getLinkedin() { return linkedin; }


    public String getProfilePicture() { return profilePicture; }
}
