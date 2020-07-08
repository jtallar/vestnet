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
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtTokenHandler implements TokenHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenHandler.class);

    /**
     * JwtToken will expire after this time.
     */
    private static final Integer ACCESS_TOKEN_EXP_MINUTES = 15; // In minutes

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
    private static final Integer REFRESH_TOKEN_EXP_MINUTES = 60; // In minutes


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
    public String createRefreshToken(LoggedUser sessionUser) {
        if (sessionUser == null || StringUtils.isBlank(sessionUser.getUsername())) {
            LOGGER.error("Cannot create JWT Token without username");
            throw new IllegalArgumentException("Cannot create JWT Token without username");
        }

        Claims claims = Jwts.claims().setSubject(sessionUser.getUsername());
        claims.put("id", sessionUser.getId());
        claims.put("roles", sessionUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));

        final ZonedDateTime now = ZonedDateTime.now();

        return Jwts.builder()
                .setClaims(claims)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(now.plusMinutes(REFRESH_TOKEN_EXP_MINUTES).toInstant()))
                .signWith(SignatureAlgorithm.HS512, TOKEN_SIGN_KEY)
                .compact();
    }

    @Override
    public LoggedUser getSessionUser(String token) {
        Jws<Claims> claimsJws;
        try {
            claimsJws = Jwts.parser()
                    .setSigningKey(TOKEN_SIGN_KEY)
                    .parseClaimsJws(token);
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException ex) {
            LOGGER.error("Invalid JWT Token");
            throw new BadCredentialsException("Invalid JWT token: ", ex);
        } catch (ExpiredJwtException ex) {
            LOGGER.error("JWT Token Expired");
            throw new JwtExpiredTokenException(token, "JWT Token expired", ex);
        }
        List<String> authorities = claimsJws.getBody().get("roles", List.class);
        return new LoggedUser(claimsJws.getBody().get("id", Long.class),
                claimsJws.getBody().getSubject(),
                authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
    }
}
