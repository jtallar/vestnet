package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.interfaces.exceptions.UserAlreadyExistsException;
import ar.edu.itba.paw.model.Message;
import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.Token;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.components.Page;

import java.util.List;
import java.util.Optional;

public interface UserService {

    /**
     * Creates a new user given the params.
     * @return The created User.
     * @throws UserAlreadyExistsException when the username is already taken.
     */
    User create (String role, String password, String firstName, String lastName, String realId,
                 Integer birthYear, Integer birthMonth, Integer birthDay,
                 Integer countryId, Integer stateId, Integer cityId,
                 String email, String phone, String linkedin, byte[] image) throws UserAlreadyExistsException;


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
     * Sets locale and returns found user.
     * @param username User's username.
     * @param locale Locale to set.
     * @return The modified user.
     */
    User setLocale(String username, String locale);


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
     * Finds the projects owned by the user.
     * @param id Unique user id.
     * @return List of all the project for the given user.
     */
    List<Project> getOwnedProjects(long id);


    /**
     * Gets all the user accepted offers.
     * @param receiverId User in matter unique id.
     * @param page Page requested.
     * @param pageSize Page size.
     * @return Messages page with the required data.
     */
    Page<Message> getAcceptedMessages(long receiverId, Integer page, Integer pageSize);


    /**
     * Gets all the user made offers.
     * @param senderId User in matter unique id.
     * @param page Page requested.
     * @param pageSize Page size.
     * @return Messages page with the required data.
     */
    Page<Message> getOfferMessages(long senderId, Integer page, Integer pageSize);


    /**
     * Gets all the unread messages for an user and a project.
     * @param userId Unique user id.
     * @param projectId Unique project id.
     * @return A list of all the messages that match criteria.
     */
    List<Message> getProjectUnreadMessages(long userId, long projectId);


    /**
     * Gets the last sent message.
     * @param userId Unique user id.
     * @param projectId Unique project id.
     * @return An optional for the last sent message.
     */
    Optional<Message> getLastProjectOfferMessage(long userId, long projectId);


    /**
     * Finds user only profile image.
     * @param id The user unique id to find its image.
     * @return The user image, default if none found.
     */
    byte[] getProfileImage(long id);


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
