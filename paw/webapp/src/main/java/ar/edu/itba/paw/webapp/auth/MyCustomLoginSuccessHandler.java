package ar.edu.itba.paw.webapp.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;

public class MyCustomLoginSuccessHandler implements AuthenticationSuccessHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyCustomLoginSuccessHandler.class);
    private static final String URL_PRIOR_LOGIN_ATTR = "url_prior_login";
    private static final String SPRING_SAVED_REQUEST_ATTR = "SPRING_SECURITY_SAVED_REQUEST";

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session != null) {
            final Enumeration<String> attributeNames = session.getAttributeNames();
            while (attributeNames.hasMoreElements()) {
                String at = attributeNames.nextElement();
                LOGGER.debug("\n\nAttribute: {}, Value: {}", at, session.getAttribute(at));
            }
            String redirectUrl = (String) session.getAttribute(URL_PRIOR_LOGIN_ATTR);
            LOGGER.debug("\n\nLink: {}", redirectUrl);
            if (redirectUrl != null) {
                // we do not forget to clean this attribute from session
                session.removeAttribute(URL_PRIOR_LOGIN_ATTR);
                // then we redirect
                redirectStrategy.sendRedirect(request, response, redirectUrl);
            } else {
                SavedRequest savedRequest = (SavedRequest) session.getAttribute(SPRING_SAVED_REQUEST_ATTR);
                if (savedRequest != null && (redirectUrl = savedRequest.getRedirectUrl()) != null) {
                    // we do not forget to clean this attribute from session
                    session.removeAttribute(SPRING_SAVED_REQUEST_ATTR);
                    // then we redirect
                    redirectStrategy.sendRedirect(request, response, redirectUrl);
                } else {
                    redirectStrategy.sendRedirect(request, response, "/");
                }
            }
        } else {
            redirectStrategy.sendRedirect(request, response, "/");
        }
    }
}