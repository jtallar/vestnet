package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.components.LoggedUser;

import java.util.Map;
import java.util.Optional;

public interface TokenHandler {

    /**
     * Returns a new access token used for further authentication and authorization
     * @param loggedUser the logged user
     * @return the token
     */
    String createAccessToken(LoggedUser loggedUser);

    /**
     * Returns a new refresh token used for further authentication and authorization
     * @param loggedUser the logged user
     * @param extended true if token should have extended lifetime
     * @return the token
     */
    String createRefreshToken(LoggedUser loggedUser, boolean extended);

    /**
     * Returns a map with a new access token and a new refresh token, as well as additional information
     * @param loggedUser the logged user
     * @param extended true if refresh token should have extended lifetime
     * @return the map with the tokens and additional information
     */
    Map<String, String> createTokenMap(LoggedUser loggedUser, boolean extended);

    /**
     * Returns the logged user from a token
     * @param token the token
     * @return the session user contained in the token
     */
    Optional<LoggedUser> getSessionUser(String token);
}
