package ar.edu.itba.paw.webapp.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class MyCustomLoginSuccessHandler implements AuthenticationSuccessHandler {
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session != null) {
            String redirectUrl = (String) session.getAttribute("url_prior_login");
            if (redirectUrl != null && !redirectUrl.contains("welcome")) {
                // we do not forget to clean this attribute from session
                session.removeAttribute("url_prior_login");
                // then we redirect
                redirectStrategy.sendRedirect(request, response, redirectUrl);
            } else {
                redirectStrategy.sendRedirect(request, response, "/");
            }
        } else {
            redirectStrategy.sendRedirect(request, response, "/");
        }
    }
}