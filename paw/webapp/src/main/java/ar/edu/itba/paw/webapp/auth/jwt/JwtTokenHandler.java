package ar.edu.itba.paw.webapp.auth.jwt;

import ar.edu.itba.paw.model.components.LoggedUser;
import ar.edu.itba.paw.webapp.exception.jwt.JwtExpiredTokenException;
import io.jsonwebtoken.*;
import ar.edu.itba.paw.interfaces.TokenHandler;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtTokenHandler implements TokenHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenHandler.class);

    /**
     * JwtToken will expire after this time.
     */
    private static final int ACCESS_TOKEN_EXP_MINUTES = 15; // In minutes

    /**
     * Key is used to sign JwtToken.
     */
    private static final String TOKEN_SIGN_KEY = "" +
            "" +
            "" +
            "" +
            "" +
            "" +
            "";

    /**
     * JwtToken can be refreshed during this timeframe.
     */
    private static final int REFRESH_TOKEN_EXP_MINUTES = 60; // In minutes == 1 hour

    /**
     * JwtToken can be refreshed during this timeframe if extended is issued.
     */
    private static final int REFRESH_TOKEN_EXTENDED_EXP_MINUTES = 10080; // In minutes == 7 days


    @Override
    public String createAccessToken(LoggedUser sessionUser) {
        if (sessionUser == null || StringUtils.isBlank(sessionUser.getUsername())) {
            LOGGER.error("Cannot create JWT Token without username");
            throw new IllegalArgumentException("Cannot create JWT Token without username");
        }
        if (sessionUser.getAuthorities() == null || sessionUser.getAuthorities().isEmpty()) {
            LOGGER.error("Cannot create JWT Token without any roles");
            throw new IllegalArgumentException("Cannot create JWT Token without any roles");
        }
        Claims claims = Jwts.claims().setSubject(sessionUser.getUsername());
        claims.put("id", sessionUser.getId());
        claims.put("roles", sessionUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));

        final ZonedDateTime now = ZonedDateTime.now();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(now.plusMinutes(ACCESS_TOKEN_EXP_MINUTES).toInstant()))
                .signWith(SignatureAlgorithm.HS512, TOKEN_SIGN_KEY)
                .compact();
    }

    @Override
    public String createRefreshToken(LoggedUser sessionUser, boolean extended) {
        if (sessionUser == null || StringUtils.isBlank(sessionUser.getUsername())) {
            LOGGER.error("Cannot create JWT Token without username");
            throw new IllegalArgumentException("Cannot create JWT Token without username");
        }

        Claims claims = Jwts.claims().setSubject(sessionUser.getUsername());
        claims.put("id", sessionUser.getId());
        claims.put("roles", sessionUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        claims.put("extended", extended);

        final ZonedDateTime now = ZonedDateTime.now();

        return Jwts.builder()
                .setClaims(claims)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(now.plusMinutes(getRefreshTokenExpMinutes(extended)).toInstant()))
                .signWith(SignatureAlgorithm.HS512, TOKEN_SIGN_KEY)
                .compact();
    }

    @Override
    public Map<String, String> createTokenMap(LoggedUser loggedUser, boolean extended) {
        Map<String, String> map = new HashMap<>();
        map.put("access_token", createAccessToken(loggedUser));
        map.put("AT_minutes_to_expire", String.valueOf(ACCESS_TOKEN_EXP_MINUTES));
        map.put("refresh_token", createRefreshToken(loggedUser, extended));
        map.put("RT_minutes_to_expire", String.valueOf(getRefreshTokenExpMinutes(extended)));
        return map;
    }

    private int getRefreshTokenExpMinutes(boolean extended) {
        return (extended) ? REFRESH_TOKEN_EXTENDED_EXP_MINUTES : REFRESH_TOKEN_EXP_MINUTES;
    }

    @Override
    public Optional<LoggedUser> getSessionUser(String token) {
        Jws<Claims> claimsJws;
        try {
            claimsJws = Jwts.parser()
                    .setSigningKey(TOKEN_SIGN_KEY)
                    .parseClaimsJws(token);
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException ex) {
            LOGGER.error("Invalid JWT Token");
//            throw new BadCredentialsException("Invalid JWT token: ", ex);
            return Optional.empty();
        } catch (ExpiredJwtException ex) {
            LOGGER.error("JWT Token Expired");
//            throw new JwtExpiredTokenException(token, "JWT Token expired", ex);
            return Optional.empty();
        }
        List<String> authorities = claimsJws.getBody().get("roles", List.class);
        return Optional.of(new LoggedUser(claimsJws.getBody().get("id", Long.class),
                claimsJws.getBody().getSubject(),
                authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())));
    }
}
