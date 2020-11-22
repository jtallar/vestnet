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

@Path("projects")
@Component
public class ProjectRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    protected SessionUserFacade sessionUser;

    @Context
    private UriInfo uriInfo;

    private static final int PAGE_SIZE = 3;
    private static final int PAGINATION_ITEMS = 5;

    @GET
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
        final Project project = projectService.create(projectDto.getName(), projectDto.getSummary(), projectDto.getCost(), /*sessionUser.getId()*/55L); // TODO fix
        final URI projectUri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(project.getId())).build();
        return Response.created(projectUri).build();
    }


    @GET
    @Path("/{id}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response project(@PathParam("id") long id) {
        return projectService.findById(id)
                .map(p -> Response.ok(ProjectDto.fromProject(p, uriInfo)).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }


    @PUT
    @Path("/{id}")
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response update(@PathParam("id") long id,
                           final ProjectDto projectDto) {
        return projectService.update(id, projectDto.getName(), projectDto.getSummary(), projectDto.getCost())
                .map(p -> Response.ok().build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }


    @GET
    @Path("/{id}/categories")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response projectCategories(@PathParam("id") long id) {
        Optional<Project> optionalProject = projectService.findById(id);

        return optionalProject.map(p -> {
            List<CategoryDto> categoriesDto = p.getCategories().stream().map(CategoryDto::fromCategory).collect(Collectors.toList());
            return Response.ok(new GenericEntity<List<CategoryDto>>(categoriesDto) {}).build();
        }).orElse(Response.status(Response.Status.NOT_FOUND).build());
    }


    @PUT
    @Path("/{id}/categories")
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response updateCategories(@PathParam("id") long id,
                                     final List<CategoryDto> categoriesDto) {
        List<Category> categories = categoriesDto.stream().map(c -> new Category(c.getId())).collect(Collectors.toList());

        return projectService.addCategories(id, categories)
                .map(p -> Response.ok().build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }


    @GET
    @Path("/categories")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response categories() {
        List<Category> categories = projectService.getAllCategories();
        List<CategoryDto> categoriesDto = categories.stream().map(CategoryDto::fromCategory).collect(Collectors.toList());

        return Response.ok(new GenericEntity<List<CategoryDto>>(categoriesDto) {}).build();
    }


    @PUT
    @Path("/{id}/hit")
    public Response addHit(@PathParam("id") long id) {
        return projectService.addHit(id)
                .map(p -> Response.ok().build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }


    @PUT
    @Path("/{id}/funded")
    public Response funded(@PathParam("id") long id) {
        return projectService.setFunded(id)
                .map(p -> Response.ok().build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }
}
