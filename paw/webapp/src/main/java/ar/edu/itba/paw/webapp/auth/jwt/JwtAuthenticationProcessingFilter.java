package ar.edu.itba.paw.webapp.auth.jwt;

import ar.edu.itba.paw.interfaces.TokenExtractor;
import ar.edu.itba.paw.webapp.config.WebAuthConfig;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JwtAuthenticationProcessingFilter extends GenericFilterBean {

    private final TokenExtractor tokenExtractor;
    private final AuthenticationProvider authenticationProvider;

    public JwtAuthenticationProcessingFilter(TokenExtractor tokenExtractor, AuthenticationProvider authenticationProvider) {
        this.tokenExtractor = tokenExtractor;
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String authorizationContent = ((HttpServletRequest) request).getHeader(WebAuthConfig.AUTH_HEADER);
        String token = tokenExtractor.extract(authorizationContent);
        SecurityContextHolder.getContext().setAuthentication(authenticationProvider.authenticate(new JwtAuthenticationToken(token)));

        chain.doFilter(request, response);
    }
}
