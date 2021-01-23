package ar.edu.itba.paw.interfaces.exceptions;

public class ImageDoesNotExistException extends ResourceDoesNotExistException {

    private static final long serialVersionUID = -8678741721272095499L;

    public ImageDoesNotExistException() {
        super();
    }

    public ImageDoesNotExistException(String message) {
        super(message);
    }

    public ImageDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImageDoesNotExistException(Throwable cause) {
        super(cause);
    }

    protected ImageDoesNotExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
