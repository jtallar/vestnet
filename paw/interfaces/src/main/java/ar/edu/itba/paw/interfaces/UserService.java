package ar.edu.itba.paw.interfaces;
import ar.edu.itba.paw.model.User;

import java.util.Optional;

public interface UserService {
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
    User create(String username);
}
