package ar.edu.itba.paw.interfaces.exceptions;

public abstract class ResourceDoesNotExistException extends Exception {

    private static final long serialVersionUID = 7291520549525891401L;

    public ResourceDoesNotExistException() {
        super();
    }

    public ResourceDoesNotExistException(String message) {
        super(message);
    }

    public ResourceDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceDoesNotExistException(Throwable cause) {
        super(cause);
    }

    protected ResourceDoesNotExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
