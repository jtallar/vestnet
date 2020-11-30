package ar.edu.itba.paw.webapp.exception;

import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    @Override
    public Response toResponse(final ConstraintViolationException e) {
        return Response.status(Response.Status.CONFLICT).build();
    }
}
