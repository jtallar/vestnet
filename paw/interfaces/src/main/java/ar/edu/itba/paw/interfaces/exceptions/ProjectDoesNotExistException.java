package ar.edu.itba.paw.interfaces.exceptions;

public class ProjectDoesNotExistException extends Exception {

    private static final long serialVersionUID = -8831120205963871767L;

    public ProjectDoesNotExistException() {
        super();
    }

    public ProjectDoesNotExistException(String message) {
        super(message);
    }

    public ProjectDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProjectDoesNotExistException(Throwable cause) {
        super(cause);
    }

    protected ProjectDoesNotExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
