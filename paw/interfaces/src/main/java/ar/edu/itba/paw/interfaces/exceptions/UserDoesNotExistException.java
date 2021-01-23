package ar.edu.itba.paw.interfaces.exceptions;

public class UserDoesNotExistException extends ResourceDoesNotExistException {

    private static final long serialVersionUID = -1434893962746952440L;

    public UserDoesNotExistException() {
        super();
    }
    public UserDoesNotExistException(String message, Throwable cause, boolean enableSuppression,
                                      boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public UserDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserDoesNotExistException(String message) {
        super(message);
    }

    public UserDoesNotExistException(Throwable cause) {
        super(cause);
    }
}
