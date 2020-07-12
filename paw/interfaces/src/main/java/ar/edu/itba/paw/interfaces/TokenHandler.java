package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.components.LoggedUser;

import java.util.Optional;

public interface TokenHandler {

    /**
     * Returns a new access token used for further authentication and authorization
     * @param sessionUser the logged user
     * @return the token
     */
    String createAccessToken(LoggedUser sessionUser);

    /**
     * Returns a new refresh token used for further authentication and authorization
     * @param sessionUser the logged user
     * @return the token
     */
    String createRefreshToken(LoggedUser sessionUser);

    /**
     * Returns the logged user from a token
     * @param token the token
     * @return the session user contained in the token
     */
    Optional<LoggedUser> getSessionUser(String token);
}
