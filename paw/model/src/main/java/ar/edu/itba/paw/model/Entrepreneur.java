package ar.edu.itba.paw.model;

import java.time.LocalDate;

public class Entrepreneur extends User {

    public Entrepreneur(long id, int role, String firstName, String lastName, String realId, LocalDate birthDate, Location location, String email, String phone, String linkedin, String profilePicture, LocalDate joinDate, int trustIndex) {
        super(id, role, firstName, lastName, realId, birthDate, location, email, phone, linkedin, profilePicture, joinDate, trustIndex);
    }
}
