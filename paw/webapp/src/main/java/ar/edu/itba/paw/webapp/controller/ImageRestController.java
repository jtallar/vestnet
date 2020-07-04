package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.ProjectService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.image.UserImage;
import ar.edu.itba.paw.webapp.dto.ImageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Component
@Path("/images")
public class ImageRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageRestController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Context
    private UriInfo uriInfo;


    // TODO: Esta bien devolver el byte[] asi sin mas o deberia hacer un fromUserImage?
    //       (en ese caso, habria que devolver UserImage y ProjectImage? O el dto tiene solo image)
    @GET
    @Path("/users/{user_id}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response getUserImage(@PathParam("user_id") final long userId) {
        return Response.ok(ImageDto.fromUserImage(new UserImage(userService.getProfileImage(userId)))).build();
    }

    @GET
    @Path("/test/{user_id}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response testGetUserImage(@PathParam("user_id") final long userId) {
        return Response.ok(userService.getProfileImage(userId)).build();
    }

    @PUT
    @Path("/users/{user_id}")
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response setUserImage(@PathParam("user_id") final long userId, final ImageDto image) {
        LOGGER.debug("\n\nImage length: {}", image.getImage().length);
        User updatedUser = userService.updateImage(userId, image.getImage());
        if (updatedUser == null) {
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        }
        return Response.ok().build();
    }

    @GET
    @Path("/projects/{project_id}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response getProjectPortrait(@PathParam("project_id") final long projectId) {
        return Response.ok(projectService.getPortraitImage(projectId)).build();
    }

    @GET
    @Path("/projects/{project_id}/slideshow")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response getProjectSlideshow(@PathParam("project_id") final long projectId) {
        return Response.ok(projectService.getSlideshowImages(projectId)).build(); // TODO: Check esto
    }
}
