package ar.edu.itba.paw.interfaces.exceptions;

public class IllegalProjectAccessException extends Exception {

    private static final long serialVersionUID = -215320898902020834L;

    public IllegalProjectAccessException() {
        super();
    }

    public IllegalProjectAccessException(String message) {
        super(message);
    }

    public IllegalProjectAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalProjectAccessException(Throwable cause) {
        super(cause);
    }

    protected IllegalProjectAccessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
