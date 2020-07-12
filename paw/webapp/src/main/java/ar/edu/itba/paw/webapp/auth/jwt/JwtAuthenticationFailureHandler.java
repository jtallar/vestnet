package ar.edu.itba.paw.webapp.auth.jwt;

import ar.edu.itba.paw.webapp.auth.jwt.error.ErrorCode;
import ar.edu.itba.paw.webapp.auth.jwt.error.ErrorResponse;
import ar.edu.itba.paw.webapp.exception.jwt.AuthMethodNotSupportedException;
import ar.edu.itba.paw.webapp.exception.jwt.JwtExpiredTokenException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFailureHandler.class);

    @Autowired
    private ObjectMapper mapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        if (e instanceof BadCredentialsException) {
            LOGGER.error("Invalid Credentials");
            mapper.writeValue(response.getWriter(), ErrorResponse.of("Invalid credentials", ErrorCode.AUTHENTICATION, HttpServletResponse.SC_UNAUTHORIZED));
        } else if (e instanceof JwtExpiredTokenException) {
            LOGGER.error("Token has expired");
            mapper.writeValue(response.getWriter(), ErrorResponse.of("Token has expired", ErrorCode.JWT_TOKEN_EXPIRED, HttpServletResponse.SC_UNAUTHORIZED));
        } else if (e instanceof AuthMethodNotSupportedException) {
            LOGGER.error("Error message: {}", e.getMessage());
            mapper.writeValue(response.getWriter(), ErrorResponse.of(e.getMessage(), ErrorCode.AUTHENTICATION, HttpServletResponse.SC_UNAUTHORIZED));
        }

        LOGGER.error("Authentication failed");
        mapper.writeValue(response.getWriter(), ErrorResponse.of("Authentication failed", ErrorCode.AUTHENTICATION, HttpServletResponse.SC_UNAUTHORIZED));
    }
}
