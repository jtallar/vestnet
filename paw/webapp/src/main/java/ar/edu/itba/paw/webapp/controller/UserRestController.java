package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.SessionUserFacade;
import ar.edu.itba.paw.interfaces.exceptions.InvalidTokenException;
import ar.edu.itba.paw.interfaces.exceptions.UserAlreadyExistsException;
import ar.edu.itba.paw.interfaces.exceptions.UserDoesNotExistException;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.components.Page;
import ar.edu.itba.paw.webapp.dto.location.LocationDto;
import ar.edu.itba.paw.webapp.component.UriInfoUtils;
import ar.edu.itba.paw.webapp.dto.user.*;
import ar.edu.itba.paw.webapp.dto.project.ProjectDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.*;
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

    /** General user endpoints */

    @POST
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response createUser(@Valid final FullUserWithPasswordDto user) throws UserAlreadyExistsException {

        LOGGER.debug("Endpoint POST /users reached with " + user.toString());

        final User newUser = userService.create(FullUserWithPasswordDto.toUser(user), UriInfoUtils.getBaseURI(uriInfo));
        final URI userUri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(newUser.getId())).build();
        return Response.created(userUri).header("Access-Control-Expose-Headers", "Location").build();
    }


    @PUT
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response updateUser(@Valid final UpdatableUserDto user) throws UserDoesNotExistException {

        LOGGER.debug("Endpoint PUT /users reached with " + user.toString() + " - User is " + sessionUser.getId());

        userService.update(sessionUser.getId(), UpdatableUserDto.toUser(user)).orElseThrow(UserDoesNotExistException::new);
        return Response.ok().build();
    }

    @DELETE
    public Response deleteUser() {

        LOGGER.debug("Endpoint DELETE /users reached - User is " + sessionUser.getId());

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }


    @GET
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response personalProfile() throws UserDoesNotExistException {

        LOGGER.debug("Endpoint GET /users reached - User is " + sessionUser.getId());

        return userProfile(sessionUser.getId());
    }


    @GET
    @Path("/{id}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response userProfile(@PathParam("id") final long id) throws UserDoesNotExistException {

        LOGGER.debug("Endpoint GET /users/" + id + " reached");

        final User user = userService.findById(id).orElseThrow(UserDoesNotExistException::new);
        return Response.ok(FullUserDto.fromUser(user, uriInfo)).build();
    }


    /** Extra user data endpoints */

    @GET
    @Path("/projects")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response ownedProjects(@QueryParam("funded") @DefaultValue("true") boolean funded,
                                  @QueryParam("p") @DefaultValue("1") int page,
                                  @QueryParam("l") @DefaultValue("3") int limit) {

        LOGGER.debug("Endpoint GET /users/projects reached - User is " + sessionUser.getId());

        return ownedProjects(sessionUser.getId(), funded, page, limit);
    }


    @GET
    @Path("/{id}/projects")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response ownedProjects(@PathParam("id") final long userId,
                                  @QueryParam("funded") @DefaultValue("true") boolean funded,
                                  @QueryParam("p") @DefaultValue("1") int page,
                                  @QueryParam("l") @DefaultValue("3") int limit) {

        LOGGER.debug("Endpoint GET /users/" + userId + "/projects/ reached");

        final Page<Project> projectPage = userService.getOwnedProjects(userId, funded, page, limit);
        final List<ProjectDto> projects = projectPage.getContent().stream().map(p -> ProjectDto.fromProject(p, uriInfo)).collect(Collectors.toList());

        return Response.ok(new GenericEntity<List<ProjectDto>>(projects) {})
                .link(uriInfo.getRequestUriBuilder().replaceQueryParam("p", 1).build(), "first")
                .link(uriInfo.getRequestUriBuilder().replaceQueryParam("p", projectPage.getStartPage()).build(), "start")
                .link(uriInfo.getRequestUriBuilder().replaceQueryParam("p", projectPage.getEndPage()).build(), "end")
                .link(uriInfo.getRequestUriBuilder().replaceQueryParam("p", projectPage.getTotalPages()).build(), "last")
                .header("Access-Control-Expose-Headers", "Link")
                .build();
    }


    @GET
    @Path("/favorites")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response getFavoritesIds() throws UserDoesNotExistException {

        LOGGER.debug("Endpoint GET /users/favorites reached - User is " + sessionUser.getId());

        final User user = userService.findById(sessionUser.getId()).orElseThrow(UserDoesNotExistException::new);
        final List<FavoriteDto> favorites = user.getFavorites().stream().map(FavoriteDto::fromFavorite).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<FavoriteDto>>(favorites) {}).build();
   }


    @GET
    @Path("/favorites/profile")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response getFavorites() throws UserDoesNotExistException {

        LOGGER.debug("Endpoint GET /users/favorites/profile reached - User is " + sessionUser.getId());

        final User user = userService.findById(sessionUser.getId()).orElseThrow(UserDoesNotExistException::new);
        final List<ProjectDto> favorites = user.getFavoriteProjects().stream().map(p -> ProjectDto.fromProject(p, uriInfo)).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<ProjectDto>>(favorites) {}).build();
    }


    @PUT
    @Path("/favorites")
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response addFavorite(@Valid final FavoriteDto favoriteDto) throws UserDoesNotExistException {

        LOGGER.debug("Endpoint PUT /users/favorites reached with " + favoriteDto.toString() + " - User is " + sessionUser.getId());

        userService.addFavorites(sessionUser.getId(), favoriteDto.getProjectId(), favoriteDto.getAdd()).orElseThrow(UserDoesNotExistException::new);
        return Response.ok().build();
    }


    /** User password and verification endpoints */

    @POST
    @Path("/password")
    public Response requestPassword(@Valid final MailDto mailDto) throws UserDoesNotExistException {

        LOGGER.debug("Endpoint POST /users/password reached with " + mailDto.toString());

        userService.requestPassword(mailDto.getMail(), UriInfoUtils.getBaseURI(uriInfo)).orElseThrow(UserDoesNotExistException::new);
        return Response.ok().build();
    }


    @PUT
    @Path("/password")
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response updatePassword(@Valid final PasswordDto passwordDto) throws InvalidTokenException {

        LOGGER.debug("Endpoint PUT /users/password reached with " + passwordDto.toString());

        userService.updatePassword(passwordDto.getToken(), passwordDto.getPassword());
        return Response.ok().build();
    }


    @POST
    @Path("/verify")
    public Response requestVerification(@Valid final MailDto mailDto) throws UserDoesNotExistException, UserAlreadyExistsException {

        LOGGER.debug("Endpoint POST /users/verify reached with " + mailDto.toString());

        userService.requestVerification(mailDto.getMail(), UriInfoUtils.getBaseURI(uriInfo)).orElseThrow(UserDoesNotExistException::new);
        return Response.ok().build();
    }


    @PUT
    @Path("/verify")
    public Response updateVerification(@Valid final TokenDto tokenDto) throws InvalidTokenException {

        LOGGER.debug("Endpoint PUT /users/verify reached with " + tokenDto.toString());

        userService.updateVerification(tokenDto.getToken(), UriInfoUtils.getBaseURI(uriInfo));
        return Response.ok().build();
    }


    @GET
    @Path("/{id}/location")
    public Response getUserLocation(@PathParam("id") final long userId) throws UserDoesNotExistException {

        LOGGER.debug("Endpoint GET /users/" + userId + "/location reached");

        final User user = userService.findById(userId).orElseThrow(UserDoesNotExistException::new);
        return Response.ok(LocationDto.fromLocation(user.getLocation())).build();
    }
}


