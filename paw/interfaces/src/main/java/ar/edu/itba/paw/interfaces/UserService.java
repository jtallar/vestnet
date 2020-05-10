package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.Location;
import ar.edu.itba.paw.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserService {

    /**
     * Creates a new user or updates its values.
     * @param role The user's role.
     * @param firstName User's first name.
     * @param lastName User's last name.
     * @param realId User's real identification. Depends for each coutnry.
     * @param birthDate User's birth date.
     * @param location User's location. Country, state and city.
     * @param email User's mail address.
     * @param phone User's phone address.
     * @param linkedin User's linkedin profile link.
     * @param password User's encrypted password.
     * @param imageBytes User's profile image.
     * @return Operation return.
     * @throws UserAlreadyExistsException When user already exist on database.
     */
    long create(String role, String firstName, String lastName, String realId, LocalDate birthDate, Location location,
                String email, String phone, String linkedin, String password, byte[] imageBytes) throws UserAlreadyExistsException;

    /**
     * Finds a user given its user name.
     * @param username The user name to search for.
     * @returnThe matched user, or null otherwise.
     */
    Optional<User> findByUsername(String username);

    /**
     * Finds a user given its id.
     * @param id The unique id for the user.
     * @return The matched user, or null otherwise.
     */
    Optional<User> findById(long id);

    /**
     * Finds all users with a name coincidence.
     * @param name The string search coincidence.
     * @return List of users that match criteria.
     */
    List<User> findCoincidence(String name);

    /**
     * Adds an encrypted password for a user.
     * @param id The user unique id.
     * @param password The password.
     * @return Operation return.
     */
    long createPassword(long id, String password);

    /**
     * Finds the image fot the given user.
     * @param userId The unique user id.
     * @return Image as a byte array.
     */
    byte[] findImageForUser(long userId);
}
