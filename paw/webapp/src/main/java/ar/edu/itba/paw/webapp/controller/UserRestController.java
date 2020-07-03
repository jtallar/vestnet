package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.exceptions.UserAlreadyExistsException;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.dto.UserDto;
import ar.edu.itba.paw.webapp.event.VerificationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;

@Component
@Path("/users")
public class UserRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserRestController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Context
    private UriInfo uriInfo;

    @POST
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response createUser(final UserDto user) {
        LOGGER.debug("\n\nBase URI: {}\n\n", uriInfo.getBaseUri().toString());

//        final User newUser;
//        try {
//            TODO: COMO ARMO LOCATION E IMAGE? HAY QUE CAMBIAR CREATE PARA QUE RECIBA UN DATE
//            newUser = userService.create(user.getRole(), user.getPassword(), user.getFirstName(), user.getLastName(),
//                    user.getRealId(), user.getBirthDate(), user.getLocation(), user.getEmail(), user.getPhone(),
//                    user.getLinkedin(), user.getImage());
//            eventPublisher.publishEvent(new VerificationEvent(newUser, uriInfo.getBaseUri().toString()));
//        } catch (UserAlreadyExistsException e) {
//            LOGGER.error("User already exists with email {} in VestNet", user.getEmail());
//            return Response.status(Response.Status.CONFLICT).build();
//        } catch (IOException e) {
//            return Response.serverError().build();
//        }
//        final URI userUri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(newUser.getId())).build();
//        return Response.created(userUri).build();

        return Response.created(uriInfo.getAbsolutePath()).build(); // TODO: REMOVE WHEN SOLVED ^
    }

    @GET
    @Path("/{id}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response userProfile(@PathParam("id") final long id) {
        final Optional<User> maybeUser = userService.findById(id);
        if (!maybeUser.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        }
        return Response.ok(maybeUser.map(user -> UserDto.fromUser(user, uriInfo)).get()).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response updateUser(@PathParam("id") final long id, final UserDto user) {

//        final User updatedUser;
//        try {
//            TODO: COMO ARMO LOCATION E IMAGE? HAY QUE HACER EL METODO UPDATE
//            updatedUser = userService.update(user.getRole(), user.getPassword(), user.getFirstName(), user.getLastName(),
//                    user.getRealId(), user.getBirthDate(), user.getLocation(), user.getEmail(), user.getPhone(),
//                    user.getLinkedin(), user.getImage());
//        } catch (UserDoesNotExistException e) {
//            LOGGER.error("User with email {} does not exist in VestNet", user.getEmail());
//            return Response.status(Response.Status.NOT_FOUND).build();
//        } catch (IOException e) {
//            return Response.serverError().build();
//        }
//        final URI userUri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(updatedUser.getId())).build();
//        return Response.created(userUri).build();

        return Response.created(uriInfo.getAbsolutePath()).build(); // TODO: REMOVE WHEN SOLVED ^
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") final long id) {
        userService.removeUser(id);   // TODO: CAMBIAR A BORRADO LOGICO
        return Response.noContent().build();
    }
}
