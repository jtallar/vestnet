package ar.edu.itba.paw.model;

import java.net.URI;
import java.util.Date;

public abstract class User {

    private final long id;
    // TODO> VER SI SON FINAL O EDITABLES. POR AHORA MANDO FINAL, CUALQUIER  COSA SACARLO
    private final String firstName;
    private final String lastName;
    private final String realId; // CUIT/CUIL/DNI
    private final Date birthDate;
    // Aca si quiero que se complete despues, debiera poner un locationId que sea final y un Location que no lo sea
    private final Location location;

    private final String email;
    private final String phone;
    private final String linkedin;

    private final Date joinDate;
    private final URI profilePicture;
    private int trustIndex;

    public User(long id, String firstName, String lastName, String realId, Date birthDate, Location location, String email, String phone, String linkedin, URI profilePicture, Date joinDate, int trustIndex) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.realId = realId;
        this.birthDate = birthDate;
        this.location = location;
        this.email = email;
        this.phone = phone;
        this.linkedin = linkedin;
        this.profilePicture = profilePicture;
        this.joinDate = joinDate;
        this.trustIndex = trustIndex;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getRealId() {
        return realId;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public Location getLocation() {
        return location;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public URI getProfilePicture() {
        return profilePicture;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public int getTrustIndex() {
        return trustIndex;
    }

    public void setTrustIndex(int trustIndex) {
        this.trustIndex = trustIndex;
    }
}
