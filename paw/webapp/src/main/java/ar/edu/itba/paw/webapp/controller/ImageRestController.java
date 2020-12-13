package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.SessionUserFacade;
import ar.edu.itba.paw.interfaces.services.ProjectService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.image.ProjectImage;
import ar.edu.itba.paw.model.image.UserImage;
import ar.edu.itba.paw.webapp.dto.ImageDto;
import ar.edu.itba.paw.webapp.dto.ProjectDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Path("/images")
public class ImageRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageRestController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private SessionUserFacade sessionUser;

    @Context
    private UriInfo uriInfo;

    // TODO: Ahora se comporta como users/image_id, deberia recibir user_id y gettear la image desde el image_id en el user
    //  como hace el update? O lo cambiamos a image_id?
    @GET
    @Path("/users/{user_id}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response getUserImage(@PathParam("user_id") final long userId) {
        Optional<UserImage> profileImage = userService.getProfileImage(userId);

        return profileImage.map(userImage -> Response.ok(ImageDto.fromUserImage(userImage)).build())
                .orElse(Response.status(Response.Status.NOT_FOUND.getStatusCode()).build());
    }

    // TODO: Ver si por concepto nomas no deberia recibir el id en el path, aunque no se use.
    //  O bien chequear que el que haga el update sea el session user
    @PUT
    @Path("/users")
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response setUserImage(@Valid final ImageDto image) {
        Optional<User> optionalUser = userService.updateImage(sessionUser.getId(), image.getImage());

        return optionalUser.map(u -> Response.ok().build())
                .orElse(Response.status(Response.Status.NOT_FOUND.getStatusCode()).build());
    }


    @GET
    @Path("/projects/{project_id}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response getProjectPortrait(@PathParam("project_id") final long id) {
        return projectService.getPortraitImage(id)
                .map(i -> Response.ok(ImageDto.fromProjectImage(i)).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @PUT
    @Path("/projects/{project_id}")
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response setProjectPortrait(@PathParam("project_id") final long id,
                                       @Valid final ImageDto image) {
        return projectService.setPortraitImage(sessionUser.getId(), id, image.getImage())
                .map(i -> Response.ok().build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    @Path("/projects/{project_id}/slideshow")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response getProjectSlideshow(@PathParam("project_id") final long id) {
        List<ProjectImage> projectImages = projectService.getSlideshowImages(id);
        List<ImageDto> images = projectImages.stream().map(ImageDto::fromProjectImage).collect(Collectors.toList());

        return Response.ok(new GenericEntity<List<ImageDto>>(images) {}).build();
    }

    // TODO: [org.springframework.orm.jpa.JpaSystemException: A collection with cascade="all-delete-orphan" was no longer referenced by the owning entity instance:
    //  ar.edu.itba.paw.model.Project.images; nested exception is org.hibernate.HibernateException: A collection with cascade="all-delete-orphan" was no longer referenced by the owning entity instance: ar.edu.itba.paw.model.Project.images]
    // TODO: Ver si el @Valid ahi va bien
    @PUT
    @Path("/projects/{project_id}/slideshow")
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response setProjectSlideshow(@PathParam("project_id") final long id,
                                        @Valid final List<ImageDto> images) {
        List<byte []> bytes = images.stream().map(ImageDto::getImage).collect(Collectors.toList());

        return projectService.setSlideshowImages(sessionUser.getId(), id, bytes)
                .map(i -> Response.ok().build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }
}
