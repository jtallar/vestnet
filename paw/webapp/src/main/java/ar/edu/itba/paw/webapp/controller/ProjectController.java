package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.SessionUserFacade;
import ar.edu.itba.paw.interfaces.services.MessageService;
import ar.edu.itba.paw.interfaces.services.ProjectService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.Category;
import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.components.Page;
import ar.edu.itba.paw.webapp.dto.CategoryDto;
import ar.edu.itba.paw.webapp.dto.ProjectDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;

@Path("test")
@Component
public class ProjectController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    protected SessionUserFacade sessionUser;

    @Context
    private UriInfo uriInfo;

    private static final int PAGE_SIZE = 3;
    private static final int PAGINATION_ITEMS = 5;

    @GET // TODO make lazy relationships if needed
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response projects(@QueryParam("p") @DefaultValue("1") int page,
                         @QueryParam("o") @DefaultValue("1") int order,
                         @QueryParam("f") @DefaultValue("1") int field,
                         @QueryParam("s") String keyword,
                         @QueryParam("max") Integer maxCost,
                         @QueryParam("min") Integer minCost,
                         @QueryParam("c") Integer category) {

        Page<Project> projectPage = projectService.findAll(category, minCost, maxCost, keyword, field, order, page, PAGE_SIZE);
        projectPage.setPageRange(PAGINATION_ITEMS);

        List<ProjectDto> projects = projectPage.getContent().stream().map(p -> ProjectDto.fromProject(p, uriInfo)).collect(Collectors.toList());


        return Response.ok(new GenericEntity<List<ProjectDto>>(projects) {})
                .link(uriInfo.getRequestUriBuilder().queryParam("p", projectPage.getStartPage()).build(), "first")
                .link(uriInfo.getRequestUriBuilder().queryParam("p", projectPage.getEndPage()).build(), "last")
                .build();
    }


    @POST
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response create(final ProjectDto projectDto) {
        final Project project = projectService.create(projectDto.getName(), projectDto.getSummary(), projectDto.getCost(), sessionUser.getId());
        final URI projectUri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(project.getId())).build();
        return Response.created(projectUri).build();
    }


    @GET
    @Path("/{projectId}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response project(@PathParam("projectId") long id) {
        Optional<Project> project = projectService.findById(id);
        return project.map(p -> Response.ok(ProjectDto.fromProject(p, uriInfo)).build()).orElseGet(() -> Response.status(404).build());
    }


    @PUT
    @Path("/{projectId}")
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response update(final ProjectDto projectDto) {
        final Optional<Project> project = projectService.update(projectDto.getId(), projectDto.getName(), projectDto.getSummary(), projectDto.getCost());
        return project.map(p -> Response.ok().build()).orElseGet(() -> Response.status(404).build());
    }


    @GET
    @Path("/{projectId}/categories")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response projectCategories(@PathParam("projectId") long id) {
        // TODO return project categories
        return Response.ok().build();
    }


    @PUT
    @Path("/{projectId}/categories")
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response updateCategories(final List<CategoryDto> categoriesDto) {
        // TODO update project's categories
        return Response.ok().build();
    }


    @GET
    @Path("/categories")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response categories() {
        List<Category> categories = projectService.getAllCategories();
        List<CategoryDto> categoriesDto = categories.stream().map(CategoryDto::fromCategory).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<CategoryDto>>(categoriesDto) {}).build();
    }

}
