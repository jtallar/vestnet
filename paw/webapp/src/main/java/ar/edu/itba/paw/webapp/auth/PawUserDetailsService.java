package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.components.LoggedUser;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.components.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

@Component
public class PawUserDetailsService implements UserDetailsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PawUserDetailsService.class);

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        final Optional<User> optionalUser =  this.userService.setLocale(username, LocaleContextHolder.getLocale().toString());
        if (!optionalUser.isPresent()) throw new UsernameNotFoundException(username + "not found");

        final User user = optionalUser.get();

        Collection<GrantedAuthority> authorities = new HashSet<>();

        switch (UserRole.valueOf(user.getRole())) {
            case ENTREPRENEUR:
                authorities.add(new SimpleGrantedAuthority("ROLE_ENTREPRENEUR"));
                break;
            case INVESTOR:
                authorities.add(new SimpleGrantedAuthority("ROLE_INVESTOR"));
                break;
            case NOTFOUND:
                LOGGER.error("Username Role not found");
                throw new UsernameNotFoundException(username + "not found");
            default:
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                authorities.add(new SimpleGrantedAuthority("ROLE_ENTREPRENEUR"));
                authorities.add(new SimpleGrantedAuthority("ROLE_INVESTOR"));
                break;
        }
        return new LoggedUser(user.getId(), user.getLocale(), username, user.getPassword(), user.isVerified(), true, true, true, authorities);
    }



}