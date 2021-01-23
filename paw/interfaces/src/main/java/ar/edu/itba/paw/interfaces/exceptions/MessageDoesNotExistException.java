package ar.edu.itba.paw.interfaces.exceptions;

public class MessageDoesNotExistException extends ResourceDoesNotExistException {

    private static final long serialVersionUID = -8193325422964291711L;

    public MessageDoesNotExistException() {
        super();
    }

    public MessageDoesNotExistException(String message) {
        super(message);
    }

    public MessageDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageDoesNotExistException(Throwable cause) {
        super(cause);
    }

    protected MessageDoesNotExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
