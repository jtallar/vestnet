package ar.edu.itba.paw.webapp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RoleCookieSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    public static final String ROLE_COOKIE_NAME = "user_roles";
    private static final Logger LOGGER = LoggerFactory.getLogger(RoleCookieSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        StringBuilder cookieText = new StringBuilder();
        authentication.getAuthorities().forEach((role) ->cookieText.append(role.getAuthority().hashCode()).append(" "));
        LOGGER.debug("\n\nuser_roles set to {}\n\n", cookieText.toString());
        Cookie roleCookie = new Cookie(ROLE_COOKIE_NAME, cookieText.toString());
//        roleCookie.setMaxAge(WebAuthConfig.TOKEN_DAYS);
//        roleCookie.setDomain("/");
//        roleCookie.setPath("/");
        response.addCookie(roleCookie);
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
