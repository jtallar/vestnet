package ar.edu.itba.paw.model.components;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class LoggedUser extends User {

    private final long id;

    public LoggedUser(long id, String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
    }

    public LoggedUser(long id, String username, Collection<? extends GrantedAuthority> authorities) {
        super(username, "", authorities); // TODO: Ver que hacemos ahi en password, hace falta? Es para JWT
        this.id = id;
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "LoggedUser{" +
                "id=" + id +
                "username=" + getUsername() +
                '}';
    }
}