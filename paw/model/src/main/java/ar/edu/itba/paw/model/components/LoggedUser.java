package ar.edu.itba.paw.model.components;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class LoggedUser extends User {

    private final long id;
    private final String locale;

    public LoggedUser(long id, String locale, String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
        this.locale = locale;
    }

    public LoggedUser(long id, String locale, String username, Collection<? extends GrantedAuthority> authorities) {
        super(username, "", authorities);
        this.id = id;
        this.locale = locale;
    }

    public long getId() {
        return id;
    }

    public String getLocale() {
        return locale;
    }

    @Override
    public String toString() {
        return "LoggedUser{" +
                "id=" + id +
                "username=" + getUsername() +
                '}';
    }
}