package ar.edu.itba.paw.webapp.auth.jwt.error;

import javax.json.Json;
import java.util.Date;

public class ErrorResponse {
    // HTTP Response Status Code
    private final int status;

    // General Error message
    private final String message;

    // Error code
    private final ErrorCode errorCode;

    private final Date timestamp;

    protected ErrorResponse(final String message, final ErrorCode errorCode, int status) {
        this.message = message;
        this.errorCode = errorCode;
        this.status = status;
        this.timestamp = new java.util.Date();
    }

    public static ErrorResponse of(final String message, final ErrorCode errorCode, int status) {
        return new ErrorResponse(message, errorCode, status);
    }

    public String getJsonResponse() {
        return Json.createObjectBuilder()
                .add("errorCode", errorCode.getErrorCode())
                .add("message", message)
                .build().toString();
    }

    public Integer getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
