package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.interfaces.exceptions.UserAlreadyExistsException;
import ar.edu.itba.paw.model.User;
import org.springframework.transaction.annotation.Transactional;

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
    Optional<User> findById(Long id);


    /**
     * Deletes a favorite from a user.
     * @param userId The user unique id.
     * @param projectId The project to remove from favorites.
     * @return The changed user, null if user not exists.
     */
    User deleteFavorite(Long userId, Long projectId);


    /**
     * Adds a favorite to a user.
     * @param userId The user unique id.
     * @param projectId The project to add to favorites.
     * @return The changed user, null if user not exists.
     */
    User addFavorite(Long userId, Long projectId);

}
