package ar.edu.itba.paw.interfaces.daos;

import ar.edu.itba.paw.interfaces.exceptions.UserAlreadyExistsException;
import ar.edu.itba.paw.model.Location;
import ar.edu.itba.paw.model.User;

import java.time.LocalDate;
import java.util.Optional;

public interface UserDao {

    /**
     * Creates a new user if possible.
     * @param role Role number.
     * @param password User's encoded password.
     * @param firstName User's first name.
     * @param lastName User's last name.
     * @param realId User's real ID, country dependant.
     * @param birthDate User's birth date.
     * @param location User's location -> country, state and city.
     * @param email User's mail. Should be unique.
     * @param phone User's phone number.
     * @param linkedin User's linkedin profile link.
     * @param image User's profile picture.
     * @return The created user.
     * @throws UserAlreadyExistsException If the user's mail already exists.
     */
     User create(Integer role, String password, String firstName, String lastName, String realId,
                LocalDate birthDate, Location location, String email, String phone, String linkedin, byte[] image) throws UserAlreadyExistsException;


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
