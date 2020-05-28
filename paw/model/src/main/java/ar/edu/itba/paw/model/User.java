package ar.edu.itba.paw.model;

import java.time.LocalDate;
//import org.springframework.security.core.userdetails.UserDetails;

/**
 * Model of the registered user on the web app.
 */
public class User {

    private String password;

    private final long id;
    private final int role;
    private final String firstName;
    private final String lastName;
    private final String realId; // Specific to country
    private final LocalDate birthDate;

    private final Location location;

    private final String email;
    private final String phone;
    private final String linkedin;

    private final LocalDate joinDate;
    private int trustIndex;
    private boolean verified;

    public User(long id, int role, String firstName, String lastName, String realId, LocalDate birthDate, Location location, String email, String phone, String linkedin, LocalDate joinDate, int trustIndex) {
        this.id = id;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.realId = realId;
        this.birthDate = birthDate;
        this.location = location;
        this.email = email;
        this.phone = phone;
        this.linkedin = linkedin;
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

    public LocalDate getBirthDate() {
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

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public int getTrustIndex() {
        return trustIndex;
    }

    public void setTrustIndex(int trustIndex) {
        this.trustIndex = trustIndex;
    }

    public int getRole() {
        return role;
    }

    public String getPassword() {
        return this.password;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public void setPassword(String password){
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", realId='" + realId + '\'' +
                ", birthDate=" + birthDate +
                ", location=" + location +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", linkedin='" + linkedin + '\'' +
                ", joinDate=" + joinDate +
                ", trustIndex=" + trustIndex +
                '}';
    }

    /**
     * User possible roles.
     */
    public enum UserRole {
        ENTREPRENEUR("Entrepreneur", 1), INVESTOR("Investor", 2),
        NOTFOUND("Not found", 0);

        private String role;
        private int id;

        UserRole(String role, int id) {
            this.role = role;
            this.id = id;
        }

        public String getRole() {
            return role;
        }

        public int getId() {
            return id;
        }

        public static UserRole valueOf(int id) {
            for (UserRole role : UserRole.values()) {
                if (role.getId() == id)
                    return role;
            }
            return NOTFOUND;
        }
    }
}
