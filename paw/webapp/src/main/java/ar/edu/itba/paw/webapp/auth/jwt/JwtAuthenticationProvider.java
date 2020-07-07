package ar.edu.itba.paw.webapp.auth.jwt;

import ar.edu.itba.paw.interfaces.TokenHandler;
import ar.edu.itba.paw.model.components.LoggedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private TokenHandler tokenHandler;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        LoggedUser loggedUser = tokenHandler.getSessionUser((String) authentication.getCredentials());
        return new JwtAuthenticationToken(loggedUser, loggedUser.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return JwtAuthenticationToken.class.isAssignableFrom(aClass);
    }
}
