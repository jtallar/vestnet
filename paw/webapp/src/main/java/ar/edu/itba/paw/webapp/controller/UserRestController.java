package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.SessionUserFacade;
import ar.edu.itba.paw.interfaces.exceptions.UserAlreadyExistsException;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.Favorite;
import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.location.Location;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    /** General user endpoints */

    @POST
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response createUser(@Valid final FullUserWithPasswordDto user) {

        LOGGER.debug("\n\nBase URI: {}\n\n", uriInfo.getBaseUri().toString());

        final User newUser;
        try {
            newUser = userService.create(FullUserWithPasswordDto.toUser(user), UriInfoUtils.getBaseURI(uriInfo));
        } catch (UserAlreadyExistsException e) {
            LOGGER.error("User already exists with email {} in VestNet", user.getEmail());
            return Response.status(Response.Status.CONFLICT).build();
        }
        final URI userUri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(newUser.getId())).build();
        return Response.created(userUri).header("Access-Control-Expose-Headers", "Location").build();
    }


    @PUT
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response updateUser(@Valid final UpdatableUserDto user) {

        Optional<User> optionalUser = userService.update(sessionUser.getId(), UpdatableUserDto.toUser(user));

        return optionalUser.map(u -> Response.ok().build())
                .orElse(Response.status(Response.Status.NOT_FOUND.getStatusCode()).build());
    }


    @DELETE
    public Response deleteUser() {

        userService.remove(sessionUser.getId());
        return Response.noContent().build();
    }


    @GET
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response personalProfile() {
        return userProfile(sessionUser.getId());
    }


    // TODO: Por algun motivo, retorna un "type":"fullUserDto" entre los atributos que devuelve, es problema?
    // FIXME: Tira un error 500 para algunos de los primeros usuarios, que no tenian validaciones. Por ejemplo, el 5 con el phoneNumber
    @GET
    @Path("/{id}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response userProfile(@PathParam("id") final long id) {

        final Optional<User> optionalUser = userService.findById(id);

        return optionalUser.map(u -> Response.ok(FullUserDto.fromUser(u, uriInfo)).build())
                .orElse(Response.status(Response.Status.NOT_FOUND.getStatusCode()).build());
    }


    /** Extra user data endpoints */

    @GET
    @Path("/{id}/projects")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response ownedProjects(@PathParam("id") final long userId,
                                  @QueryParam("funded") @DefaultValue("true") boolean funded) {

        List<Project> projectsList = userService.getOwnedProjects(userId, funded);
        List<ProjectDto> projects = projectsList.stream().map(p -> ProjectDto.fromProject(p, uriInfo)).collect(Collectors.toList());

        return Response.ok(new GenericEntity<List<ProjectDto>>(projects) {}).build();
    }


    @GET
    @Path("/favorites")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response getFavoritesIds() {

        Optional<User> optionalUser = userService.findById(sessionUser.getId());

        return optionalUser.map(u -> {
            List<FavoriteDto> favorites = optionalUser.get().getFavorites().stream().map(FavoriteDto::fromFavorite).collect(Collectors.toList());
            return Response.ok(new GenericEntity<List<FavoriteDto>>(favorites) {}).build();
        }).orElse(Response.status(Response.Status.NOT_FOUND).build());

   }


    @GET
    @Path("/favorites/profile")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response getFavorites() {

        Optional<User> optionalUser = userService.findById(sessionUser.getId());

        return optionalUser.map(u -> {
            List<ProjectDto> favorites = optionalUser.get().getFavoriteProjects().stream().map(p -> ProjectDto.fromProject(p, uriInfo)).collect(Collectors.toList());
            return Response.ok(new GenericEntity<List<ProjectDto>>(favorites) {}).build();
        }).orElse(Response.status(Response.Status.NOT_FOUND).build());
    }


    @PUT
    @Path("/favorites")
    public Response addFavorite(@QueryParam("add") @DefaultValue("true") boolean add,
                                @Valid final FavoriteDto favoriteDto) {

        Optional<User> optionalUser = userService.addFavorites(sessionUser.getId(), favoriteDto.getProjectId(), add);

        return optionalUser.map(u -> Response.ok().build())
                .orElse(Response.status(Response.Status.NOT_FOUND.getStatusCode()).build());
    }


    /** User password and verification endpoints */

    @POST
    @Path("/password")
    public Response requestPassword(@Valid final MailDto mailDto) {
        Optional<User> optionalUser = userService.requestPassword(mailDto.getMail(), UriInfoUtils.getBaseURI(uriInfo));

        return optionalUser.map(u -> Response.ok().build())
                .orElse(Response.status(Response.Status.NOT_FOUND.getStatusCode()).build());
    }


    @PUT
    @Path("/password")
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response updatePassword(@Valid final PasswordDto passwordDto) {

        if (userService.updatePassword(passwordDto.getToken(), passwordDto.getPassword()))
            return Response.ok().build();
        return Response.status(Response.Status.BAD_REQUEST).build();
    }


    @PUT
    @Path("/verify")
    public Response updateVerification(@Valid final TokenDto tokenDto) {

        if (userService.updateVerification(tokenDto.getToken(), UriInfoUtils.getBaseURI(uriInfo)))
            return Response.ok().build();
        return Response.status(Response.Status.BAD_REQUEST).build();
    }


    @GET
    @Path("/{id}/location")
    public Response getUserLocation(@PathParam("id") final long userId){
        final Optional<User> user = userService.findById(userId);

        return user.map(User::getLocation).map(l -> Response.ok(LocationDto.fromLocation(l)).build())
                .orElse(Response.status(Response.Status.NOT_FOUND.getStatusCode()).build());

    }
}


