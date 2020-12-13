package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.SessionUserFacade;
import ar.edu.itba.paw.interfaces.exceptions.UserAlreadyExistsException;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.dto.user.*;
import ar.edu.itba.paw.webapp.dto.ProjectDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Path("/users")
public class UserRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserRestController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private SessionUserFacade sessionUser;

    @Context
    private UriInfo uriInfo;

    @POST
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response createUser(@Valid final FullUserWithPasswordDto user) {
        LOGGER.debug("\n\nBase URI: {}\n\n", uriInfo.getBaseUri().toString());

        final User newUser; // TODO work with optional? reduces code more
        try {
            newUser = userService.create(FullUserWithPasswordDto.toUser(user), uriInfo.getBaseUri());
        } catch (UserAlreadyExistsException e) {
            LOGGER.error("User already exists with email {} in VestNet", user.getEmail());
            return Response.status(Response.Status.CONFLICT).build();
        }
        final URI userUri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(newUser.getId())).build();
        return Response.created(userUri).build();
    }


    // TODO: Ver si por concepto nomas no deberia recibir el id en el path, aunque no se use.
    //  O bien chequear que el que haga el update sea el session user
    @PUT
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response updateUser(@Valid final UpdatableUserDto user) {
        Optional<User> optionalUser = userService.update(sessionUser.getId(), UpdatableUserDto.toUser(user));

        return optionalUser.map(u -> Response.ok().build())
                .orElse(Response.status(Response.Status.NOT_FOUND.getStatusCode()).build());
    }


    // TODO: Ver si por concepto nomas no deberia recibir el id en el path, aunque no se use
    //  O bien chequear que el que haga el delete sea el session user
    @DELETE
    public Response deleteUser() {
        userService.remove(sessionUser.getId());
        return Response.noContent().build();
    }


    // TODO: Por algun motivo, retorna un "type":"fullUserDto" entre los atributos que devuelve, es problema?
    @GET
    @Path("/{id}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response userProfile(@PathParam("id") final long id) {
        final Optional<User> optionalUser = userService.findById(id);

        return optionalUser.map(u -> Response.ok(FullUserDto.fromUser(u, uriInfo)).build())
                .orElse(Response.status(Response.Status.NOT_FOUND.getStatusCode()).build());
    }


    @GET
    @Path("/{id}/projects")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response ownedProjects(@PathParam("id") final long userId,
                                  @QueryParam("funded") @DefaultValue("true") boolean funded) {
        List<Project> projectsList = userService.getOwnedProjects(userId, funded); // TODO should not be necessary put sessionUser.getId()
        List<ProjectDto> projects = projectsList.stream().map(p -> ProjectDto.fromProject(p, uriInfo)).collect(Collectors.toList());

        return Response.ok(new GenericEntity<List<ProjectDto>>(projects) {}).build();
    }

    // TODO make a GET favorites?

    @PUT
    @Path("/favorites")
    public Response favorites(@QueryParam("project") @DefaultValue("-1") long projectId, // TODO do something here
                                @QueryParam("add") @DefaultValue("true") boolean add) {
        Optional<User> optionalUser = userService.favorites(sessionUser.getId(), projectId, add);

        return optionalUser.map(u -> Response.ok().build())
                .orElse(Response.status(Response.Status.NOT_FOUND.getStatusCode()).build());
    }


    @POST
    @Path("/password")
    public Response requestPassword(@QueryParam("mail") final String mail) {
        Optional<User> optionalUser = userService.requestPassword(mail, uriInfo.getBaseUri());

        return optionalUser.map(u -> Response.ok().build())
                .orElse(Response.status(Response.Status.NOT_FOUND.getStatusCode()).build());
    }


    @PUT
    @Path("/password")
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response updatePassword(@QueryParam("token") final String token,
                                   @Valid final PasswordDto password) {
        if (userService.updatePassword(token, password.getPassword()))
            return Response.ok().build();
        return Response.status(Response.Status.BAD_REQUEST).build();
    }


    @POST
    @Path("/verify")
    public Response requestVerification(@QueryParam("mail") final String mail) {
        Optional<User> optionalUser = userService.requestVerification(mail, uriInfo.getBaseUri());

        return optionalUser.map(u -> Response.ok().build())
                .orElse(Response.status(Response.Status.NOT_FOUND.getStatusCode()).build());
    }


    @PUT
    @Path("/verify")
    public Response updateVerification(@QueryParam("token") final String token) {
        if (userService.updateVerification(token, uriInfo.getBaseUri()))
            return Response.ok().build();
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}


