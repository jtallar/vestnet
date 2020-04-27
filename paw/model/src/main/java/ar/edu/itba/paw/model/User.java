package ar.edu.itba.paw.model;

import java.time.LocalDate;

public class User {

    private String password;

    private final long id;
    // TODO: VER SI PUEDO GUARDAR UN USERROLE --> NO PUEDO MAPEAR 2 a INVESTOR(2)
    private final int role;
    // TODO> VER SI SON FINAL O EDITABLES. POR AHORA MANDO FINAL, CUALQUIER  COSA SACARLO
    private final String firstName;
    private final String lastName;
    private final String realId; // CUIT/CUIL/DNI
    private final LocalDate birthDate;
    // Aca si quiero que se complete despues, debiera poner un locationId que sea final y un Location que no lo sea
    private final Location location;

    private final String email;
    private final String phone;
    private final String linkedin;

    private final LocalDate joinDate;
    private final String profilePicture; //if we need URI then change it later
    private int trustIndex;

    public User(long id, int role, String firstName, String lastName, String realId, LocalDate birthDate, Location location, String email, String phone, String linkedin, String profilePicture, LocalDate joinDate, int trustIndex) {
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

    public String getProfilePicture() {
        return profilePicture;
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
                ", profilePicture='" + profilePicture + '\'' +
                ", trustIndex=" + trustIndex +
                '}';
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password){
        this.password = password;
    }

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
