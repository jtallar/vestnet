package ar.edu.itba.paw.webapp.auth.jwt;

import ar.edu.itba.paw.interfaces.TokenExtractor;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;

@Component
public class JwtHeaderTokenExtractor implements TokenExtractor {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtHeaderTokenExtractor.class);

    private static final String HEADER_PREFIX = "Bearer ";

    public String extract(String header) {
        if (StringUtils.isBlank(header)) {
            LOGGER.error("Authorization header is blank");
//            throw new AuthenticationServiceException("Authorization header cannot be blank!");
            return null;
        }
        if (header.length() < HEADER_PREFIX.length()) {
            LOGGER.error("Invalid authorization header size");
//             throw new AuthenticationServiceException("Invalid authorization header size.");
            return null;
        }
        return header.substring(HEADER_PREFIX.length());
    }

}
