package ar.edu.itba.paw.webapp.exception.jwt;

import org.springframework.security.core.AuthenticationException;

public class JwtExpiredTokenException extends AuthenticationException {

    private static final long serialVersionUID = -5728621054885438280L;

    private String token;

    public JwtExpiredTokenException(String msg) {
        super(msg);
    }

    public JwtExpiredTokenException(String token, String msg, Throwable t) {
        super(msg, t);
        this.token = token;
    }

    public String token() {
        return this.token;
    }
}

