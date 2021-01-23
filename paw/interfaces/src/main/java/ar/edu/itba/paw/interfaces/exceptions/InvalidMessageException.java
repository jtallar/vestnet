package ar.edu.itba.paw.interfaces.exceptions;

public class InvalidMessageException extends Exception {

    private static final long serialVersionUID = -6263383598986164887L;

    public InvalidMessageException() {
        super();
    }

    public InvalidMessageException(String message) {
        super(message);
    }

    public InvalidMessageException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidMessageException(Throwable cause) {
        super(cause);
    }

    protected InvalidMessageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
