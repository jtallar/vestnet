package ar.edu.itba.paw.webapp.exception;

import ar.edu.itba.paw.interfaces.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Iterator;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionMapper.class);

    @Override
    public Response toResponse(Throwable thr) {

        LOGGER.error("Exception thrown: {}", thr.getStackTrace()[1]);

        /* Custom Exceptions */
        if (thr instanceof UserAlreadyExistsException)
            return Response.status(Response.Status.CONFLICT).entity("").build();

        if (thr instanceof ResourceDoesNotExistException || thr instanceof IllegalProjectAccessException)
            return Response.status(Response.Status.NOT_FOUND).entity("").build();

        if (thr instanceof InvalidTokenException || thr instanceof InvalidMessageException)
            return Response.status(Response.Status.BAD_REQUEST).entity("").build();

        /* Validation Exception */
        if (thr instanceof ConstraintViolationException)
            return Response.status(Response.Status.BAD_REQUEST).entity(prepareMessage((ConstraintViolationException) thr)).build();

        /* Jersey WebApplication Exceptions */
        if (thr instanceof WebApplicationException)
            return Response.status(((WebApplicationException) thr).getResponse().getStatus()).entity("").build();

        LOGGER.error("Exception not caught: {}", (Object[]) thr.getStackTrace());

        /* Default treatment for uncaught exceptions */
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("").build();
    }


    /* Auxiliary Methods */


    /**
     * Message prepare for ConstraintViolationException.
     * @param e The thrown ConstraintViolationException.
     * @return The message to be sent.
     */
    private String prepareMessage(ConstraintViolationException e) {
        StringBuilder message = new StringBuilder();
        message.append("{ \"errors\": [\n");
        final Iterator<ConstraintViolation<?>> iterator = e.getConstraintViolations().iterator();
        while (iterator.hasNext()) {
            final ConstraintViolation<?> cv = iterator.next();
            message.append("{\"").append(getPropertyName(cv)).append("\":\"")
                    .append(cv.getMessage()).append("\"}");
            if (iterator.hasNext()) message.append(',');
            message.append('\n');
        }
        message.append("] }");
        return message.toString();
    }


    /**
     * Gets the property name from a ConstraintViolation.
     * @param cv The ConstraintViolation.
     * @return The property name throwing the violation.
     */
    private String getPropertyName(final ConstraintViolation<?> cv) {
        Path.Node last = null;
        for (Path.Node node : cv.getPropertyPath()) last = node;
        return last == null ? "" : last.getName();
    }
}
