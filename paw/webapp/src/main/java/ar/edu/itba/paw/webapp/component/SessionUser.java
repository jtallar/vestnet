package ar.edu.itba.paw.webapp.component;

import ar.edu.itba.paw.interfaces.SessionUserFacade;
import ar.edu.itba.paw.model.components.LoggedUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SessionUser implements SessionUserFacade {

    @Override
    public boolean isAnonymous() {
        return getRoles().contains("ROLE_ANONYMOUS");
    }

    @Override
    public boolean isInvestor() {
        return getRoles().contains("ROLE_INVESTOR");
    }

    @Override
    public boolean isEntrepreneur() {
        return getRoles().contains("ROLE_ENTREPRENEUR");
    }

    @Override
    public long getId() {
       LoggedUser loggedUser = (LoggedUser) getAuthentication().getPrincipal();
       return loggedUser.getId();
    }

    @Override
    public String getMail() {
        LoggedUser loggedUser = (LoggedUser) getAuthentication().getPrincipal();
        return loggedUser.getUsername();
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private List<String> getRoles() {
        return getAuthentication().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
    }

}
