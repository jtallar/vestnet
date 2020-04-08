package ar.edu.itba.paw.model;

import java.net.URI;
import java.util.Date;

public class Entrepreneur extends User {

    public Entrepreneur(long id, String firstName, String lastName, String realId, Date birthDate, Location location, String email, String phone, String linkedin, URI profilePicture, Date joinDate, int trustIndex) {
        super(id, firstName, lastName, realId, birthDate, location, email, phone, linkedin, profilePicture, joinDate, trustIndex);
    }
}
