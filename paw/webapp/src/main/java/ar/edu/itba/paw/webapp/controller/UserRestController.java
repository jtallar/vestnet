package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.SessionUserFacade;
import ar.edu.itba.paw.interfaces.exceptions.InvalidTokenException;
import ar.edu.itba.paw.interfaces.exceptions.UserAlreadyExistsException;
import ar.edu.itba.paw.interfaces.exceptions.UserDoesNotExistException;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.Favorite;
import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.image.UserImage;
import ar.edu.itba.paw.model.location.City;
import ar.edu.itba.paw.model.location.Country;
import ar.edu.itba.paw.model.location.Location;
import ar.edu.itba.paw.model.location.State;
import ar.edu.itba.paw.webapp.dto.location.LocationDto;
import ar.edu.itba.paw.webapp.component.UriInfoUtils;
import ar.edu.itba.paw.webapp.dto.CategoryDto;
import ar.edu.itba.paw.webapp.dto.user.*;
import ar.edu.itba.paw.webapp.dto.project.ProjectDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

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

        final User newUser = userService.create(FullUserWithPasswordDto.toUser(user), UriInfoUtils.getBaseURI(uriInfo));
        final URI userUri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(newUser.getId())).build();
        return Response.created(userUri).header("Access-Control-Expose-Headers", "Location").build();
    }


    @PUT
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response updateUser(@Valid final UpdatableUserDto user) throws UserDoesNotExistException {

        userService.update(sessionUser.getId(), UpdatableUserDto.toUser(user)).orElseThrow(UserDoesNotExistException::new);
        return Response.ok().build();
    }

    // TODO: Ver si lo fletamos, si lo implementamos o ninguna de las dos
    @DELETE
    public Response deleteUser() {

        userService.remove(sessionUser.getId());
        return Response.noContent().build();
    }


    @GET
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response personalProfile() throws UserDoesNotExistException{

        return userProfile(sessionUser.getId());
    }


    @GET
    @Path("/{id}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response userProfile(@PathParam("id") final long id) throws UserDoesNotExistException {

        final User user = userService.findById(id).orElseThrow(UserDoesNotExistException::new);
        return Response.ok(FullUserDto.fromUser(user, uriInfo)).build();
    }


    /** Extra user data endpoints */

    @GET
    @Path("/projects")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response ownedProjects(@QueryParam("funded") @DefaultValue("true") boolean funded) {

        return ownedProjects(sessionUser.getId(), funded);
    }

    // TODO: Pagination to avoid overloading dashboard
    @GET
    @Path("/{id}/projects")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response ownedProjects(@PathParam("id") final long userId,
                                  @QueryParam("funded") @DefaultValue("true") boolean funded) {

        final List<Project> projectsList = userService.getOwnedProjects(userId, funded);
        final List<ProjectDto> projects = projectsList.stream().map(p -> ProjectDto.fromProject(p, uriInfo)).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<ProjectDto>>(projects) {}).build();
    }


    @GET
    @Path("/favorites")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response getFavoritesIds() throws UserDoesNotExistException {

        final User user = userService.findById(sessionUser.getId()).orElseThrow(UserDoesNotExistException::new);
        final List<FavoriteDto> favorites = user.getFavorites().stream().map(FavoriteDto::fromFavorite).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<FavoriteDto>>(favorites) {}).build();
   }


    @GET
    @Path("/favorites/profile")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response getFavorites() throws UserDoesNotExistException {

        final User user = userService.findById(sessionUser.getId()).orElseThrow(UserDoesNotExistException::new);
        final List<ProjectDto> favorites = user.getFavoriteProjects().stream().map(p -> ProjectDto.fromProject(p, uriInfo)).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<ProjectDto>>(favorites) {}).build();
    }


    @PUT
    @Path("/favorites")
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response addFavorite(@Valid final FavoriteDto favoriteDto) throws UserDoesNotExistException {

        userService.addFavorites(sessionUser.getId(), favoriteDto.getProjectId(), favoriteDto.getAdd()).orElseThrow(UserDoesNotExistException::new);
        return Response.ok().build();
    }


    /** User password and verification endpoints */

    @POST
    @Path("/password")
    public Response requestPassword(@Valid final MailDto mailDto) throws UserDoesNotExistException {

        userService.requestPassword(mailDto.getMail(), UriInfoUtils.getBaseURI(uriInfo)).orElseThrow(UserDoesNotExistException::new);
        return Response.ok().build();
    }


    @PUT
    @Path("/password")
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response updatePassword(@Valid final PasswordDto passwordDto) throws InvalidTokenException {

        userService.updatePassword(passwordDto.getToken(), passwordDto.getPassword());
        return Response.ok().build();
    }


    @PUT
    @Path("/verify")
    public Response updateVerification(@Valid final TokenDto tokenDto) throws InvalidTokenException {

        userService.updateVerification(tokenDto.getToken(), UriInfoUtils.getBaseURI(uriInfo));
        return Response.ok().build();
    }


    @GET
    @Path("/{id}/location")
    public Response getUserLocation(@PathParam("id") final long userId) throws UserDoesNotExistException {

        final User user = userService.findById(userId).orElseThrow(UserDoesNotExistException::new);
        return Response.ok(LocationDto.fromLocation(user.getLocation())).build();
    }
}


