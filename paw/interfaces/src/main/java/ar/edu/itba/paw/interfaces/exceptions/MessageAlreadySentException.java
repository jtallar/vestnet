package ar.edu.itba.paw.interfaces.exceptions;

/**
 * Exception when user sends a message again.
 */
public class MessageAlreadySentException extends Exception {

    private static final long serialVersionUID = 6268973312348253201L;

    public MessageAlreadySentException() {
        super();
    }

    public MessageAlreadySentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public MessageAlreadySentException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageAlreadySentException(String message) {
        super(message);
    }

    public MessageAlreadySentException(Throwable cause) {
        super(cause);
    }
}
