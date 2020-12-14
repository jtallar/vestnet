package ar.edu.itba.paw.webapp.exception;

import org.glassfish.jersey.server.ParamException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ParamExceptionMapper implements ExceptionMapper<ParamException> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParamExceptionMapper.class);

    @Override
    public Response toResponse(ParamException e) {
        return Response.status(Response.Status.NOT_FOUND).entity("").build();
    }
}
