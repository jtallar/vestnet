package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.SessionUserFacade;
import ar.edu.itba.paw.interfaces.exceptions.IllegalProjectAccessException;
import ar.edu.itba.paw.interfaces.exceptions.ImageDoesNotExistException;
import ar.edu.itba.paw.interfaces.exceptions.ProjectDoesNotExistException;
import ar.edu.itba.paw.interfaces.exceptions.UserDoesNotExistException;
import ar.edu.itba.paw.interfaces.services.ProjectService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.image.ProjectImage;
import ar.edu.itba.paw.model.image.UserImage;
import ar.edu.itba.paw.webapp.dto.ImageDto;
import ar.edu.itba.paw.webapp.dto.SlideshowDto;
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


    @GET
    @Path("/users/{image_id}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response getUserImage(@PathParam("image_id") final long imageId) throws ImageDoesNotExistException {

        LOGGER.debug("Endpoint GET /images/users/" + imageId + " reached");

        final UserImage profileImage = userService.getImage(imageId).orElseThrow(ImageDoesNotExistException::new);
        return Response.ok(ImageDto.fromUserImage(profileImage)).build();
    }

    @PUT
    @Path("/users")
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response setUserImage(@Valid final ImageDto image) throws UserDoesNotExistException {

        LOGGER.debug("Endpoint PUT /images/users reached with " + image.toString());

        userService.setImage(sessionUser.getId(), image.getImage()).orElseThrow(UserDoesNotExistException::new);
        return Response.ok().build();
    }


    @GET
    @Path("/projects/{project_id}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response getProjectPortrait(@PathParam("project_id") final long id) throws ImageDoesNotExistException {

        LOGGER.debug("Endpoint GET /images/projects/" + id + " reached");

        final ProjectImage projectImage = projectService.getPortraitImage(id).orElseThrow(ImageDoesNotExistException::new);
        return Response.ok(ImageDto.fromProjectImage(projectImage)).build();
    }


    @PUT
    @Path("/projects/{project_id}")
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response setProjectPortrait(@PathParam("project_id") final long id,
                                       @Valid final ImageDto image) throws ProjectDoesNotExistException, IllegalProjectAccessException {

        LOGGER.debug("Endpoint PUT /images/projects/" + id + " reached with" + image.toString());

        projectService.setPortraitImage(sessionUser.getId(), id, image.getImage()).orElseThrow(ProjectDoesNotExistException::new);
        return Response.ok().build();
    }


    @GET
    @Path("/projects/{project_id}/slideshow")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response getProjectSlideshow(@PathParam("project_id") final long id) {

        LOGGER.debug("Endpoint GET /images/projects/" + id + "/slideshow reached");

        final List<ProjectImage> projectImages = projectService.getSlideshowImages(id);
        final List<ImageDto> images = projectImages.stream().map(ImageDto::fromProjectImage).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<ImageDto>>(images) {}).build();
    }


    @PUT
    @Path("/projects/{project_id}/slideshow")
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response setProjectSlideshow(@PathParam("project_id") final long id,
                                        @Valid final SlideshowDto slideshowDto) throws ProjectDoesNotExistException, IllegalProjectAccessException {

        LOGGER.debug("Endpoint PUT /images/projects/" + id + "/slideshow reached with" + slideshowDto.toString());

        final List<byte []> bytes = slideshowDto.getSlideshow().stream().map(ImageDto::getImage).collect(Collectors.toList());
        projectService.setSlideshowImages(sessionUser.getId(), id, bytes).orElseThrow(ProjectDoesNotExistException::new);
        return Response.ok().build();
    }
}
