package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.interfaces.exceptions.InvalidTokenException;
import ar.edu.itba.paw.interfaces.exceptions.UserAlreadyExistsException;
import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.components.Page;
import ar.edu.itba.paw.model.image.UserImage;

import java.net.URI;
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
    void remove(long id);


    /**
     * Adds or deletes a favorite to a user.
     * @param userId The user unique id.
     * @param projectId The project to add or delete to favorites.
     * @param add If true then adds, if not deletes.
     * @return The changed user, null if user not exists.
     */
    Optional<User> addFavorites(long userId, long projectId, boolean add);


    /**
     * Finds the projects owned by the user.
     * @param id Unique user id.
     * @param closed Distinguishes from founded project from the ones not.
     * @param page The number of page to be shown.
     * @param pageSize Page size for the pagination request.
     * @return Paged project owned for the given user.
     */
    Page<Project> getOwnedProjects(long id, boolean closed, int page, int pageSize);


    /**
     * Requests for a password change.
     * @param mail The users mail to change the password.
     * @param baseUri Base uri for mail creation.
     * @return The optional of the found user.
     */
    Optional<User> requestPassword(String mail, URI baseUri);


    /**
     * Requests for a verification mail.
     * @param mail The users mail to send the verification mail.
     * @param baseUri Base uri for mail creation.
     * @return The optional of the found user.
     * @throws UserAlreadyExistsException In case already exists a user verified.
     */
    Optional<User> requestVerification(String mail, URI baseUri) throws UserAlreadyExistsException;


    /**
     * Updates a user password.
     * @param token The token to check for.
     * @param password New user's password.
     * @throws InvalidTokenException If the token does not exists or is invalid.
     */
    void updatePassword(String token, String password) throws InvalidTokenException;


    /**
     * Sets a user as verified if the token is valid.
     * If the token exists but is invalid, resend email.
     * @param token The token.
     * @param baseUri The uri to resend the email.
     * @throws InvalidTokenException If the token does not exists or is invalid.
     */
    void updateVerification(String token, URI baseUri) throws InvalidTokenException;


    /**
     * Finds user only profile image.
     * @param id The image unique id to find it.
     * @return The user image, default if none found.
     */
    Optional<UserImage> getImage(long id);


    /**
     * Update a user image.
     * @param id User's id
     * @param image Image byte array.
     * @return The optional of the updated user.
     */
    Optional<User> setImage(long id, byte[] image);


    /**
     * Sets locale and returns found user.
     * @param username User's username.
     * @param locale Locale to set.
     * @return The optional of the modified user.
     */
    Optional<User> setLocale(String username, String locale);

}
