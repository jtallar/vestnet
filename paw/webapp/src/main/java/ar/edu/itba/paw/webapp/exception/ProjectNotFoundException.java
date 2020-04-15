package ar.edu.itba.paw.webapp.exception;

public class ProjectNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -3649021160490076964L;

    public ProjectNotFoundException() {
        super();
    }

    public ProjectNotFoundException(String message, Throwable cause, boolean enableSuppression,
                                 boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ProjectNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProjectNotFoundException(String message) {
        super(message);
    }

    public ProjectNotFoundException(Throwable cause) {
        super(cause);
    }
}
