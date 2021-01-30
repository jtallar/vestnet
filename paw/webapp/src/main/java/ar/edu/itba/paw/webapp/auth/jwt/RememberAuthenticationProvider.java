package ar.edu.itba.paw.webapp.auth.jwt;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.Assert;

public class RememberAuthenticationProvider extends DaoAuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication prevAuthentication) throws AuthenticationException {
        Assert.isInstanceOf(RememberUsernamePasswordAuthenticationToken.class, prevAuthentication, this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.onlySupports", "Only RememberUsernamePasswordAuthenticationToken is supported"));
        final Authentication newAuthentication = super.authenticate(prevAuthentication);
        return new RememberUsernamePasswordAuthenticationToken(newAuthentication.getPrincipal(), newAuthentication.getCredentials(), ((RememberUsernamePasswordAuthenticationToken) prevAuthentication).isRemember());
    }
}
