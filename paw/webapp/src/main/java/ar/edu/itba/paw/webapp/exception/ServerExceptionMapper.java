package ar.edu.itba.paw.webapp.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ServerExceptionMapper implements ExceptionMapper<ServerErrorException> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerExceptionMapper.class);

    @Override
    public Response toResponse(ServerErrorException e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("").build();
    }
}
