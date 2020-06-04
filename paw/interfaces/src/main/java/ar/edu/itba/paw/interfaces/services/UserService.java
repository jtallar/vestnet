package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.interfaces.exceptions.UserAlreadyExistsException;
import ar.edu.itba.paw.model.User;

import java.util.Optional;

public interface UserService {


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
    Optional<User> findById(Long id);
}
