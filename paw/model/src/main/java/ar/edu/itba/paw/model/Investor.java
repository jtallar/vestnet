package ar.edu.itba.paw.model;

import java.net.URI;
import java.util.Date;

public class Investor extends User {

    public Investor(long id, String firstName, String lastName, String realId, Date birthDate, Location location, String email, String phone, String linkedin, String profilePicture, Date joinDate, int trustIndex) {
        super(id, firstName, lastName, realId, birthDate, location, email, phone, linkedin, profilePicture, joinDate, trustIndex);
    }
}
