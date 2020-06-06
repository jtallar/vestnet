package ar.edu.itba.paw.interfaces.daos;

import ar.edu.itba.paw.model.Token;
import ar.edu.itba.paw.model.User;

import java.util.Optional;

public interface TokenDao {

    /**
     * Creates a new token linked to a user.
     * @param user User's token.
     * @return The created token.
     */
    Token create(User user);

    /**
     * Search for a specific token.
     * @param token The token string to look for.
     * @return The found token.
     */
    Optional<Token> findByToken(String token);

    /**
     * Deletes all the outdated tokens.
     * @return The amount of tokens deleted.
     */
    int deleteExpired();

}
