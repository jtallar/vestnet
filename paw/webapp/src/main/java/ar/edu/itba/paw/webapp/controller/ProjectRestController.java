package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.SessionUserFacade;
import ar.edu.itba.paw.interfaces.services.ProjectService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.Category;
import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.components.Page;
import ar.edu.itba.paw.webapp.dto.CategoryDto;
import ar.edu.itba.paw.webapp.dto.project.ProjectDto;
import ar.edu.itba.paw.webapp.dto.project.ProjectStagesDto;
import ar.edu.itba.paw.webapp.dto.project.ProjectStatsDto;
import ar.edu.itba.paw.webapp.dto.project.ProjectWithCategoryDto;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;
import java.util.Optional;
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

    private static final int PAGINATION_ITEMS = 5;

    /** General endpoints for projects */

    @POST
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response create(@Valid final ProjectWithCategoryDto projectDto) {

        List<Category> categories = projectDto.getCategories().stream().map(c -> new Category(c.getId())).collect(Collectors.toList());

        final Project project = projectService.create(projectDto.getName(), projectDto.getSummary(), projectDto.getFundingTarget(), categories, sessionUser.getId());
        final URI projectUri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(project.getId())).build();

        return Response.created(projectUri).header("Access-Control-Expose-Headers", "Location").build();
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

        Optional<Project> optionalProject = projectService.update(sessionUser.getId(), id,
                projectDto.getName(), projectDto.getSummary(), projectDto.getFundingTarget());

        return optionalProject.map(p -> Response.ok().build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }


    /** Search project endpoint */

    @GET
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response projects(@QueryParam("p") @DefaultValue("1") int page,
                             @QueryParam("o") @DefaultValue("1") int order,
                             @QueryParam("f") @DefaultValue("1") int field,
                             @Size(max = 50) @QueryParam("s") String keyword,
                             @Min(0) @QueryParam("max") Integer maxFundingTarget,
                             @Min(0) @QueryParam("min") Integer minFundingTarget,
                             @QueryParam("c") Integer category,
                             @QueryParam("l") @DefaultValue("3") int limit) {

        Page<Project> projectPage = projectService.findAll(category, minFundingTarget, maxFundingTarget, keyword, field, order, page, limit);
        projectPage.setPageRange(PAGINATION_ITEMS);

        List<ProjectDto> projects = projectPage.getContent().stream().map(p -> ProjectDto.fromProject(p, uriInfo)).collect(Collectors.toList());

        return Response.ok(new GenericEntity<List<ProjectDto>>(projects) {})
                .link(uriInfo.getRequestUriBuilder().queryParam("p", projectPage.getStartPage()).build(), "first")
                .link(uriInfo.getRequestUriBuilder().queryParam("p", projectPage.getEndPage()).build(), "last")
                .header("Access-Control-Expose-Headers", "Link")
                .build();
    }

    /** Extra data for projects */

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
    @Path("/{id}/stats")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response getStats(@PathParam("id") long id) {

//      TODO implement on stats
//        Optional<Project> optionalProject = projectService.findById(id);
//        return optionalProject.map(p -> Response.ok(ProjectStatsDto.fromProjectStats(p.getStats())))
//                .orElse(Response.status(Response.Status.NOT_FOUND).build());

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }


    @PUT
    @Path("/{id}/stats")
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response setStats(@PathParam("id") long id,
                             @Valid final ProjectStatsDto statsDto) {

//        TODO implement on stats
//          return projectService.addStats(id, statsDto.getSecondsAvg(), statsDto.getClicksAvg(), sessionUser.isInvestor(), statsDto.getContactClicks() == 1)
//                .map(p -> Response.ok().build())
//                .orElse(Response.status(Response.Status.NOT_FOUND).build());

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }


    @GET
    @Path("/{id}/stages")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response getStages(@PathParam("id") long id) {

//      TODO implement on stages
//        Optional<Project> optionalProject = projectService.findById(id);
//        return optionalProject.map(p -> {
//            List<ProjectStagesDto> stagesDto = p.getStages().stream().map(ProjectStagesDto::fromProjectStages).collect(Collectors.toList());
//            return Response.ok(new GenericEntity<List<ProjectStagesDto>>(stagesDto) {}).build();
//        }).orElse(Response.status(Response.Status.NOT_FOUND).build());

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }


    @PUT
    @Path("/{id}/stages")
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response setStages(@PathParam("id") long id,
                              @Valid final ProjectStagesDto stagesDto) {

//        TODO implement on stages
//          return projectService.setStage(id, stagesDto.getComment())
//                .map(p -> Response.ok().build())
//                .orElse(Response.status(Response.Status.NOT_FOUND).build());

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }


    @PUT
    @Path("/{id}/close")
    public Response close(@PathParam("id") long id) {

        return projectService.setClosed(sessionUser.getId(), id)
                .map(p -> Response.ok().build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }


    /** Get all the available categories */

    @GET
    @Path("/categories")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response categories() {

        List<Category> categories = projectService.getAllCategories();
        List<CategoryDto> categoriesDto = categories.stream().map(CategoryDto::fromCategory).collect(Collectors.toList());

        return Response.ok(new GenericEntity<List<CategoryDto>>(categoriesDto) {}).build();
    }
}
