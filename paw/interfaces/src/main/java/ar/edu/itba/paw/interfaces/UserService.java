package ar.edu.itba.paw.interfaces;
import ar.edu.itba.paw.model.Location;
import ar.edu.itba.paw.model.User;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> findByUsername(String username);
    /**
     * Finds a user given its id.
     * @param id The unique id for the user.
     * @return The matched user, or null otherwise.
     */
    Optional<User> findById(long id);

    /**
     * Create a new user.
     * @param username The name of the user.
     * @return The created user.
     */
    public List<User> findCoincidence(String name);
    long create(String role, String firstName, String lastName, String realId, LocalDate birthDate, Location location, String email, String phone, String linkedin, String profilePicture, String password);

    long createPassword(long id, String password);
}
