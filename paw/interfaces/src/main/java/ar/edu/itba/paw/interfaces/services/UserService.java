package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.interfaces.exceptions.UserAlreadyExistsException;
import ar.edu.itba.paw.interfaces.exceptions.UserDoesNotExistException;
import ar.edu.itba.paw.model.Message;
import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.Token;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.components.Page;
import ar.edu.itba.paw.model.image.UserImage;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface UserService {

    /**
     * Creates a new user given the params, for REST API.
     * @param dataUser The new user data to create.
     * @param baseUri Base URI for verification purposes.
     * @return The created User.
     * @throws UserAlreadyExistsException when the username is already taken.
     */
    User create(User dataUser, URI baseUri) throws UserAlreadyExistsException;


    /**
     * Updates an existing user given the params.
     * @param id The id of the user to update its data.
     * @param dataUser The users data to update
     * @return The created User.
     */
    Optional<User> update(long id, User dataUser) ;


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
     * Delete a user given its ID.
     * @param id The unique user's ID
     */
    void removeUser(long id);

    /**
     * Updates a user password.
     * @param mail User's mail.
     * @param password New user's password.
     * @return The updated user, null if not exist.
     */
    User updatePassword(String mail, String password);

    /**
     * Update a user image.
     * @param id User's id
     * @param image Image byte array.
     * @return The updated user, null if not exist.
     */
    User updateImage(long id, byte[] image);

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
     * Adds or deletes a favorite to a user.
     * @param userId The user unique id.
     * @param projectId The project to add or delete to favorites.
     * @param add If true then adds, if not deletes.
     * @return The changed user, null if user not exists.
     */
    Optional<User> favorites(long userId, long projectId, boolean add);


    /**
     * Finds the projects owned by the user.
     * @param id Unique user id.
     * @param funded Distinguishes from founded project from the ones not.
     * @return List of all the project for the given user.
     */
    List<Project> getOwnedProjects(long id, boolean funded);


    /**
     * Gets all the user accepted offers.
     * @param receiverId User in matter unique id.
     * @param page Page requested.
     * @param pageSize Page size.
     * @return Messages page with the required data.
     */
    Page<Message> getAcceptedMessages(long receiverId, int page, int pageSize);


    /**
     * Gets all the user made offers.
     * @param senderId User in matter unique id.
     * @param page Page requested.
     * @param pageSize Page size.
     * @return Messages page with the required data.
     */
    Page<Message> getOfferMessages(long senderId, int page, int pageSize);


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
     * @param id The image unique id to find it.
     * @return The user image, default if none found.
     */
    Optional<UserImage> getProfileImage(long id);


    /**
     * Finds the token for the given string.
     * @param token The string token to search.
     * @return Found token, if there is.
     */
    Optional<Token> findToken(String token);
}
