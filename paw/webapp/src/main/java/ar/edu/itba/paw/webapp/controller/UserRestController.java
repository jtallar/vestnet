package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.exceptions.UserAlreadyExistsException;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.dto.UserDto;
import ar.edu.itba.paw.webapp.event.VerificationEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.util.Optional;

@Component
@Path("/users")
public class UserRestController {
    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Context
    private UriInfo uriInfo;

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

//    @POST
//    @Produces(value = { MediaType.APPLICATION_JSON })
//    public Response create(final UserDto user) {
//        try {
//            User newUser = userService.create(user.getRole(), user.getPassword(), user.getFirstName(), user.getLastName(),
//                    user.getRealId(), user.getYear(), user.getMonth(), user.getDay(),
//                    user.getCountry(), user.getState(),user.getCity(),
//                    user.getEmail(), user.getPhone(), user.getLinkedin(), user.getProfilePicture().getBytes());
//
//            eventPublisher.publishEvent(new VerificationEvent(newUser, getBaseUrl(request)));
//        } catch (UserAlreadyExistsException e) {
//            LOGGER.error("User already exists with email {} in VestNet", userFields.getEmail());
//            return signUp(userFields, true);
//        } catch (IOException e) {
//            return signUp(userFields, false);
//        }
//    }
}
