package ar.edu.itba.paw.webapp.exception;


public class UserNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 676070613730583976L;

    public UserNotFoundException() {
        super();
    }

    public UserNotFoundException(String message, Throwable cause, boolean enableSuppression,
                                 boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(Throwable cause) {
        super(cause);
    }
}
