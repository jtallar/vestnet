package ar.edu.itba.paw.webapp.auth.jwt;

import ar.edu.itba.paw.model.components.JwtTokenResponse;
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

    private static final String ROLES_KEY = "roles", USER_ID_KEY = "userId", EXTENDED_KEY = "extended";

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
        claims.put(USER_ID_KEY, sessionUser.getId());
        claims.put(ROLES_KEY, sessionUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));

        final ZonedDateTime now = ZonedDateTime.now();

        return Jwts.builder()
                .setClaims(claims)
                .setId(JwtToken.ACCESS_TOKEN.id)
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
        claims.put(USER_ID_KEY, sessionUser.getId());
        claims.put(ROLES_KEY, sessionUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        claims.put(EXTENDED_KEY, extended);

        final ZonedDateTime now = ZonedDateTime.now();

        return Jwts.builder()
                .setClaims(claims)
                .setId(JwtToken.REFRESH_TOKEN.id)
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(now.plusMinutes(getRefreshTokenExpMinutes(extended)).toInstant()))
                .signWith(SignatureAlgorithm.HS512, TOKEN_SIGN_KEY)
                .compact();
    }

    @Override
    public JwtTokenResponse createTokenResponse(LoggedUser loggedUser, boolean extended) {
        String accessToken = createAccessToken(loggedUser);
        String refreshToken = createRefreshToken(loggedUser, extended);
        return new JwtTokenResponse(accessToken, ACCESS_TOKEN_EXP_MINUTES, refreshToken, getRefreshTokenExpMinutes(extended));
    }

    private int getRefreshTokenExpMinutes(boolean extended) {
        return (extended) ? REFRESH_TOKEN_EXTENDED_EXP_MINUTES : REFRESH_TOKEN_EXP_MINUTES;
    }

    @Override
    public Optional<LoggedUser> getSessionUser(String token) {
        Jws<Claims> claimsJws = getClaims(token);
        if (claimsJws == null) return Optional.empty();
        if (!claimsJws.getBody().getId().equals(JwtToken.ACCESS_TOKEN.id)) {
            LOGGER.error("Not an access token");
            return Optional.empty();
        }

        List<String> authorities = claimsJws.getBody().get(ROLES_KEY, List.class);
        return Optional.of(new LoggedUser(claimsJws.getBody().get(USER_ID_KEY, Long.class),
                claimsJws.getBody().getSubject(),
                authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())));
    }

    @Override
    public JwtTokenResponse refreshTokens(String token) {
        Jws<Claims> claimsJws = getClaims(token);
        if (claimsJws == null) return null;
        if (!claimsJws.getBody().getId().equals(JwtToken.REFRESH_TOKEN.id)) {
            LOGGER.error("Not a refresh token");
            return null;
        }

        boolean extended = claimsJws.getBody().get(EXTENDED_KEY, Boolean.class);
        List<String> authorities = claimsJws.getBody().get(ROLES_KEY, List.class);
        LoggedUser user = new LoggedUser(claimsJws.getBody().get(USER_ID_KEY, Long.class),
                claimsJws.getBody().getSubject(),
                authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));

        return createTokenResponse(user, extended);
    }

    private Jws<Claims> getClaims(String token) {
        Jws<Claims> claimsJws;
        try {
            claimsJws = Jwts.parser()
                    .setSigningKey(TOKEN_SIGN_KEY)
                    .parseClaimsJws(token);
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException ex) {
            LOGGER.error("Invalid JWT Token");
//            throw new BadCredentialsException("Invalid JWT token: ", ex);
            return null;
        } catch (ExpiredJwtException ex) {
            LOGGER.error("JWT Token Expired");
            throw new JwtExpiredTokenException(token, "JWT Token expired", ex);
//            return null;
        }
        return claimsJws;
    }

    private enum JwtToken {
        ACCESS_TOKEN("f7a3e7c662c13fc2833287026631ab1dc08b4fa8"),
        REFRESH_TOKEN("ee5faf8caf6ee6e602b53152063e8bb46a6f51a1");

        private String id;

        JwtToken(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }
}
