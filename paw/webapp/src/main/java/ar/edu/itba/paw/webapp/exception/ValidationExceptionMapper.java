package ar.edu.itba.paw.webapp.exception;

import ar.edu.itba.paw.webapp.controller.UserRestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Iterator;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidationExceptionMapper.class);

    @Override
    public Response toResponse(final ConstraintViolationException e) {
        LOGGER.error("Validation exception: {}", (Object[]) e.getStackTrace());
        return Response.status(Response.Status.BAD_REQUEST).entity(prepareMessage(e)).build();
    }

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

    private String getPropertyName(final ConstraintViolation<?> cv) {
        Path.Node last = null;
        for (Path.Node node : cv.getPropertyPath()) {
            last = node;
        }
        return last == null ? "" : last.getName();
    }
}
