package ar.edu.itba.paw.webapp.auth.jwt;

import ar.edu.itba.paw.interfaces.TokenHandler;
import ar.edu.itba.paw.model.components.LoggedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Primary
public class JwtAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private TokenHandler tokenHandler;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Optional<LoggedUser> maybeLoggedUser = tokenHandler.getSessionUser((String) authentication.getCredentials());
        if (!maybeLoggedUser.isPresent()) {
            return null;
        }
        LoggedUser loggedUser = maybeLoggedUser.get();
        return new JwtAuthenticationToken(loggedUser, loggedUser.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return JwtAuthenticationToken.class.isAssignableFrom(aClass);
    }
}
