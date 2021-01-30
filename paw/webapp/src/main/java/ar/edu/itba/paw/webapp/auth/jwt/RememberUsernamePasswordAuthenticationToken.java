package ar.edu.itba.paw.webapp.auth.jwt;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class RememberUsernamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private boolean remember;

    public RememberUsernamePasswordAuthenticationToken(Object principal, Object credentials, boolean remember) {
        super(principal, credentials);
        this.remember = remember;
    }

    public RememberUsernamePasswordAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, boolean remember) {
        super(principal, credentials, authorities);
        this.remember = remember;
    }

    public boolean isRemember() {
        return remember;
    }
}
