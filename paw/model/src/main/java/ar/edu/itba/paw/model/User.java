package ar.edu.itba.paw.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_user_id_seq")
    @SequenceGenerator(sequenceName = "users_user_id_seq", name = "users_user_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "role_id")
    private Integer role;

    @Column(name = "password", length = 76, nullable = false)
    private String password;

    @Column(name = "first_name", length = 25, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 25, nullable = false)
    private String lastName;

    @Column(name = "real_id", length = 15, nullable = false)
    private String realId; // Specific to country

    @Column(name = "aux_date")
    private LocalDate birthDate;

//    @Column(name = "aux_date")
//    private Location location;

    @Column(name = "email", length = 255, nullable = false)
    private String email;

    @Column(name = "phone", length = 25)
    private String phone;

    @Column(name = "linkedin", length = 100)
    private String linkedin;

    @Column(name = "profile_pic")
    private byte[] image;

    @Column(name = "join_date")
    private LocalDate joinDate;

    @Column(name = "verified", nullable = false)
    private boolean verified;

    /** Package */ User() {
        /** For Hibernate only */
    }

    public User(Integer role, String password, String firstName, String lastName, String realId, LocalDate birthDate,
                Location location, String email, String phone, String linkedin, byte[] image) {
        this.role = role;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.realId = realId;
        this.birthDate = birthDate;
//        this.location = location;
        this.email = email;
        this.phone = phone;
        this.linkedin = linkedin;
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

//    public Location getLocation() {
//        return location;
//    }
//
//    public void setLocation(Location location) {
//        this.location = location;
//    }

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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", realId='" + realId + '\'' +
                ", birthDate=" + birthDate +
//                ", location=" + location +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", linkedin='" + linkedin + '\'' +
                ", joinDate=" + joinDate +
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
