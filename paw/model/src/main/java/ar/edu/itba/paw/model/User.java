package ar.edu.itba.paw.model;

import ar.edu.itba.paw.model.image.UserImage;
import ar.edu.itba.paw.model.location.Location;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_seq")
    @SequenceGenerator(sequenceName = "users_id_seq", name = "users_id_seq", allocationSize = 1)
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

    @Temporal(TemporalType.DATE)
    @Column(name = "aux_date")
    private Date birthDate;

    @Embedded
    private Location location;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone", length = 25)
    private String phone;

    @Column(name = "linkedin", length = 100)
    private String linkedin;

    @Temporal(TemporalType.DATE)
    @Column(name = "join_date", insertable = false)
    private Date joinDate;

    @Column(name = "verified", nullable = false)
    private boolean verified;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
    private UserImage image;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "owner")
    private List<Project> projectList;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "receiver")
    private List<Message> receivedMessages;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "sender")
    private List<Message> sentMessages;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "favorites",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id"))
    private List<Project> favorites;

    /** Package */ User() {
        /** For Hibernate only */
    }

    public User(Integer role, String password, String firstName, String lastName, String realId, Date birthDate,
                Location location, String email, String phone, String linkedin) {
        this.role = role;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.realId = realId;
        this.birthDate = birthDate;
        this.location = location;
        this.email = email;
        this.phone = phone;
        this.linkedin = linkedin;
        this.verified = false;
    }

    public User(Long id) {
        this.id = id;
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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
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

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public UserImage getImage() {
        return image;
    }

    public void setImage(UserImage image) {
        this.image = image;
    }

    public List<Project> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<Project> projectList) {
        this.projectList = projectList;
    }

    public List<Message> getReceivedMessages() {
        return receivedMessages;
    }

    public void setReceivedMessages(List<Message> receivedMessages) {
        this.receivedMessages = receivedMessages;
    }

    public List<Message> getSentMessages() {
        return sentMessages;
    }

    public void setSentMessages(List<Message> sentMessages) {
        this.sentMessages = sentMessages;
    }

    public List<Project> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<Project> favorites) {
        this.favorites = favorites;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", role=" + role +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", realId='" + realId + '\'' +
                ", birthDate=" + birthDate +
                ", location=" + location +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", linkedin='" + linkedin + '\'' +
                ", joinDate=" + joinDate +
                ", verified=" + verified +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }

    /**
     * User possible roles.
     */
    public enum UserRole {
        ENTREPRENEUR("Entrepreneur", 1),
        INVESTOR("Investor", 2),
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

        public static UserRole getEnum(String value) {
            for (UserRole role : UserRole.values()) {
                if (role.getRole().equals(value))
                    return role;
            }
            return NOTFOUND;
        }
    }
}
