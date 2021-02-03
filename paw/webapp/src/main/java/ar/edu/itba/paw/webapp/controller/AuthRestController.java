package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.TokenExtractor;
import ar.edu.itba.paw.interfaces.TokenHandler;
import ar.edu.itba.paw.model.components.JwtTokenResponse;
import ar.edu.itba.paw.webapp.config.WebAuthConfig;
import ar.edu.itba.paw.webapp.dto.JwtTokenDto;
import ar.edu.itba.paw.webapp.exception.jwt.JwtExpiredTokenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;

@Component
@Path("/auth")
public class AuthRestController {
    @Autowired
    private TokenExtractor tokenExtractor;

    @Autowired
    private TokenHandler tokenHandler;

    @GET
    @Path("/refresh")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response refreshTokens(@Context HttpHeaders httpHeaders) {
        String token = tokenExtractor.extract(httpHeaders.getHeaderString(WebAuthConfig.AUTH_HEADER));
        JwtTokenResponse newTokens;
        try {
            newTokens = tokenHandler.refreshTokens(token);
        } catch (JwtExpiredTokenException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        if (newTokens == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.ok(JwtTokenDto.fromJwtTokenResponse(newTokens)).build();
    }
}
