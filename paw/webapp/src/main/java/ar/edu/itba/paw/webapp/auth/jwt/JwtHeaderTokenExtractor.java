package ar.edu.itba.paw.webapp.auth.jwt;

import ar.edu.itba.paw.interfaces.TokenExtractor;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;

@Component
public class JwtHeaderTokenExtractor implements TokenExtractor {
    private static final String HEADER_PREFIX = "Bearer ";

    public String extract(String header) {
        if (StringUtils.isBlank(header)) {
            throw new AuthenticationServiceException("Authorization header cannot be blank!");
        }
        if (header.length() < HEADER_PREFIX.length()) {
             throw new AuthenticationServiceException("Invalid authorization header size.");
        }
        return header.substring(HEADER_PREFIX.length());
    }

}
