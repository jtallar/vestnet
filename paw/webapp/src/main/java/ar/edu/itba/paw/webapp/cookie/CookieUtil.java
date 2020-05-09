package ar.edu.itba.paw.webapp.cookie;

import org.springframework.security.core.Authentication;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {
    public static final String ROLE_COOKIE_NAME = "user_roles";

    public static void generateRoleCookie(HttpServletResponse response, Authentication authentication) {
        if (authentication == null || response == null) return;
        StringBuilder cookieText = new StringBuilder();
        authentication.getAuthorities().forEach((role) ->cookieText.append(role.getAuthority().hashCode()).append(" "));
        Cookie roleCookie = new Cookie(ROLE_COOKIE_NAME, cookieText.toString());
        response.addCookie(roleCookie);
    }
}
