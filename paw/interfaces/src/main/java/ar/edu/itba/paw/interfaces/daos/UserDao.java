package ar.edu.itba.paw.interfaces.daos;

import ar.edu.itba.paw.interfaces.exceptions.UserAlreadyExistsException;
import ar.edu.itba.paw.model.image.UserImage;
import ar.edu.itba.paw.model.location.Location;
import ar.edu.itba.paw.model.User;

import java.util.Date;
import java.util.Optional;

public interface UserDao {

    /**
     * Creates a new user if possible.
     * @param user The user with the populated fields.
     * @return The created user.
     */
     User create(User user);

    /**
     * Delete a user given its ID.
     * @param id The unique user's ID
     */
    void removeUser(long id);

    /**
     * Finds a user given its username / mail.
     * @param username The user's username / mail.
     * @return The user with the given username / mail, null otherwise
     */
     Optional<User> findByUsername(String username);


    /**
     * Finds a user given its ID.
     * @param id The unique user's ID
     * @return The user optional.
     */
     Optional<User> findById(Long id);
}
