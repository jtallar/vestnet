package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.interfaces.exceptions.UserAlreadyExistsException;
import ar.edu.itba.paw.model.Token;
import ar.edu.itba.paw.model.User;

import java.util.Optional;
import java.util.function.ToDoubleBiFunction;

public interface UserService {

    /**
     * Creates a new user given the params.
     * @return The created User.
     * @throws UserAlreadyExistsException when the username is already taken.
     */
    User create (String role, String password, String firstName, String lastName, String realId,
                 Integer birthYear, Integer birthMonth, Integer birthDay,
                 Integer countryId, Integer stateId, Integer cityId,
                 String email, String phone, String linkedin, long imageId) throws UserAlreadyExistsException;


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
    Optional<User> findById(long id);


    /**
     * Updates a user password.
     * @param mail User's mail.
     * @param password New user's password.
     * @return The updated user, null if not exist.
     */
    User updatePassword(String mail, String password);


    /**
     * Sets a user as verified.
     * @param id User's unique id.
     * @return The verified user, null if not found.
     */
    User verifyUser(long id);


    /**
     * Deletes a favorite from a user.
     * @param userId The user unique id.
     * @param projectId The project to remove from favorites.
     * @return The changed user, null if user not exists.
     */
    User deleteFavorite(long userId, long projectId);


    /**
     * Adds a favorite to a user.
     * @param userId The user unique id.
     * @param projectId The project to add to favorites.
     * @return The changed user, null if user not exists.
     */
    User addFavorite(long userId, long projectId);


    /**
     * Creates a token for a user.
     * @param userId The unique user id.
     * @return The created token
     */
    Token createToken(long userId);

    /**
     * Finds the token for the given string.
     * @param token The string token to search.
     * @return Found token, if there is.
     */
    Optional<Token> findToken(String token);

}
