package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;


@Component
public class PawUserDetailsService implements UserDetailsService{
    private static final Logger LOGGER = LoggerFactory.getLogger(PawUserDetailsService.class);

    @Autowired
    private UserService us;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        final User user =  us.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username + "not found"));
        Collection<GrantedAuthority> authorities = new HashSet<>();

        switch (User.UserRole.valueOf(user.getRole())) {
            case ENTREPRENEUR:
                authorities.add(new SimpleGrantedAuthority("ROLE_ENTREPRENEUR"));
                break;
            case INVESTOR:
                authorities.add(new SimpleGrantedAuthority("ROLE_INVESTOR"));
                break;
            case NOTFOUND:
                // TODO: VER QUE PONGO ACA, NO DEBERIA PASAR NUNCA
                throw new UsernameNotFoundException(username + "not found");
            default:
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                authorities.add(new SimpleGrantedAuthority("ROLE_ENTREPRENEUR"));
                authorities.add(new SimpleGrantedAuthority("ROLE_INVESTOR"));
                break;
        }
        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), authorities);
    }
}