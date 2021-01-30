package ar.edu.itba.paw.webapp.auth.jwt;

import ar.edu.itba.paw.interfaces.TokenExtractor;
import ar.edu.itba.paw.webapp.auth.jwt.error.ErrorCode;
import ar.edu.itba.paw.webapp.auth.jwt.error.ErrorResponse;
import ar.edu.itba.paw.webapp.config.WebAuthConfig;
import ar.edu.itba.paw.webapp.exception.jwt.JwtExpiredTokenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationProcessingFilter extends GenericFilterBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationProcessingFilter.class);

    private final TokenExtractor tokenExtractor;
    private final AuthenticationProvider authenticationProvider;

    public JwtAuthenticationProcessingFilter(TokenExtractor tokenExtractor, AuthenticationProvider authenticationProvider) {
        this.tokenExtractor = tokenExtractor;
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            String authorizationContent = ((HttpServletRequest) request).getHeader(WebAuthConfig.AUTH_HEADER);
            String token = tokenExtractor.extract(authorizationContent);
            SecurityContextHolder.getContext().setAuthentication(authenticationProvider.authenticate(new JwtAuthenticationToken(token)));

            chain.doFilter(request, response);
        } catch (JwtExpiredTokenException e) {
            // If token expired, returns 401 --> Must refresh tokens
            LOGGER.error(e.getMessage());
            final ErrorResponse errResponse = ErrorResponse.of("Token has expired", ErrorCode.JWT_TOKEN_EXPIRED, HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(errResponse.getJsonResponse());
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setStatus(errResponse.getStatus());
        }
    }
}
