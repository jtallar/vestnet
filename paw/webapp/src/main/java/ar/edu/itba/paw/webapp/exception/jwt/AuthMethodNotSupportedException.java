package ar.edu.itba.paw.webapp.exception.jwt;

import org.springframework.security.authentication.AuthenticationServiceException;

public class AuthMethodNotSupportedException extends AuthenticationServiceException {
    private static final long serialVersionUID = -333963691479118405L;

    public AuthMethodNotSupportedException(String msg) {
        super(msg);
    }
}
