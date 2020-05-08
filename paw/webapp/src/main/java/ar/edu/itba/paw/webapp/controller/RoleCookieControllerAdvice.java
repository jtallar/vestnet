package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.config.RoleCookieSuccessHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@ControllerAdvice
public class RoleCookieControllerAdvice {
    // TODO: VER QUE PASA SI TOCO STOP Y PLAY --> LE QUEDA LA COOKIE (SOLO MUESTRA UN HEADER DISTINTO, NADA MAS)
    @ModelAttribute("roleNumber")
    public int roleNumber(HttpServletRequest request) {
        final Cookie cookie = WebUtils.getCookie(request, RoleCookieSuccessHandler.ROLE_COOKIE_NAME);
        if (cookie != null) {
            final List<String> roles = Arrays.asList(cookie.getValue().split(" "));;
            if (roles.contains(String.valueOf("ROLE_ENTREPRENEUR".hashCode())))
                return User.UserRole.ENTREPRENEUR.getId();
            if (roles.contains(String.valueOf("ROLE_INVESTOR".hashCode())))
                return User.UserRole.INVESTOR.getId();
        }
        return User.UserRole.NOTFOUND.getId();
    }
}