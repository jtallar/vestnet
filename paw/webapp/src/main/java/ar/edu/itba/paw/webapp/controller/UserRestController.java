package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.exceptions.UserAlreadyExistsException;
import ar.edu.itba.paw.interfaces.exceptions.UserDoesNotExistException;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.dto.UserDto;
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
import java.net.URI;
import java.util.Optional;

@Component
@Path("/users")
public class UserRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserRestController.class);

    @Autowired
    private UserService userService;

    @Context
    private UriInfo uriInfo;

    @POST
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response createUser(final UserDto user) {
        LOGGER.debug("\n\nBase URI: {}\n\n", uriInfo.getBaseUri().toString());

        final User newUser;
        try {
            newUser = userService.create(user.getRole(), user.getPassword(), user.getFirstName(), user.getLastName(),
                    user.getRealId(), user.getBirthDate(), user.getCountryId(), user.getStateId(), user.getCityId(),
                    user.getEmail(), user.getPhone(), user.getLinkedin(), null);

            // TODO event publisher removed. It did:
            // User user = event.getUser();
            // String token = userService.createToken(user.getId()).getToken();
            // emailService.sendVerification(user, token, event.getBaseUrl());

        } catch (UserAlreadyExistsException e) {
            LOGGER.error("User already exists with email {} in VestNet", user.getEmail());
            return Response.status(Response.Status.CONFLICT).build();
        }
        final URI userUri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(newUser.getId())).build();
        return Response.created(userUri).build();
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

        try {
            userService.update(id, user.getFirstName(), user.getLastName(), user.getRealId(), user.getBirthDate(),
                    user.getCountryId(), user.getStateId(), user.getCityId(), user.getPhone(), user.getLinkedin());
        } catch (UserDoesNotExistException e) {
            LOGGER.error("User with email {} does not exist in VestNet", user.getEmail());
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().build();
    }

//    TODO: DELETE THROWS ERROR CAUSE TOKEN STILL REFERENCES USER
//    @DELETE
//    @Path("/{id}")
//    public Response deleteUser(@PathParam("id") final long id) {
//        userService.removeUser(id);   // TODO: CAMBIAR A BORRADO LOGICO?
//        return Response.noContent().build();
//    }


    // TODO getOwnedProjects. Here or in projectRestController?
    // userService.getOwnedProjects(sessionUser.getId(), funded)

    // TODO addFavorite. Project
    // userService.addFavorite(sessionUser.getId(), projectId);

    // TODO deleteFavorite. Project
    // userService.deleteFavorite(sessionUser.getId(), projectId);
}
