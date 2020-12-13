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
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectRestController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    protected SessionUserFacade sessionUser;

    @Context
    private UriInfo uriInfo;

    private static final String DEFAULT_PAGE_SIZE = "3";
    private static final int PAGINATION_ITEMS = 5;

    // TODO: Ver si no conviene que reciba tambien el page size para saber cuantos proyectos quiere
    @GET
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response projects(@QueryParam("p") @DefaultValue("1") int page,
                         @QueryParam("o") @DefaultValue("1") int order,
                         @QueryParam("f") @DefaultValue("1") int field,
                         @Size(max = 50) @QueryParam("s") String keyword,
                         @Min(0) @QueryParam("max") Integer maxCost,
                         @Min(0) @QueryParam("min") Integer minCost,
                         @QueryParam("c") Integer category,
                         @QueryParam("l") @DefaultValue(DEFAULT_PAGE_SIZE) int limit) {

        Page<Project> projectPage = projectService.findAll(category, minCost, maxCost, keyword, field, order, page, limit);
        projectPage.setPageRange(PAGINATION_ITEMS);

        List<ProjectDto> projects = projectPage.getContent().stream().map(p -> ProjectDto.fromProject(p, uriInfo)).collect(Collectors.toList());

        return Response.ok(new GenericEntity<List<ProjectDto>>(projects) {})
                .link(uriInfo.getRequestUriBuilder().queryParam("p", projectPage.getStartPage()).build(), "first")
                .link(uriInfo.getRequestUriBuilder().queryParam("p", projectPage.getEndPage()).build(), "last")
                .build();
    }


    @POST
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response create(@Valid final ProjectDto projectDto) {
        final Project project = projectService.create(projectDto.getName(), projectDto.getSummary(), projectDto.getCost(), sessionUser.getId());
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
                           @Valid final ProjectDto projectDto) {
        Optional<Project> optionalProject = projectService.update(sessionUser.getId(), id, projectDto.getName(), projectDto.getSummary(), projectDto.getCost());

        return optionalProject.map(p -> Response.ok().build())
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


    // Recibe lista como -d'[{"id":14,"name":"3D_Printers","parent":1},...]'
    @PUT
    @Path("/{id}/categories")
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response updateCategories(@PathParam("id") long id,
                                     @NotEmpty final List<CategoryDto> categoriesDto) {
        List<Category> categories = categoriesDto.stream().map(c -> new Category(c.getId())).collect(Collectors.toList());

        return projectService.addCategories(sessionUser.getId(), id, categories)
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
        return projectService.setFunded(sessionUser.getId(), id)
                .map(p -> Response.ok().build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }
}
