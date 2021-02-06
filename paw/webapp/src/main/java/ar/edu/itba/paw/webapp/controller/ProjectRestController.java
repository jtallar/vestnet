package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.SessionUserFacade;
import ar.edu.itba.paw.interfaces.exceptions.IllegalProjectAccessException;
import ar.edu.itba.paw.interfaces.exceptions.ProjectDoesNotExistException;
import ar.edu.itba.paw.interfaces.services.ProjectService;
import ar.edu.itba.paw.model.Category;
import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.components.Page;
import ar.edu.itba.paw.webapp.dto.CategoryDto;
import ar.edu.itba.paw.webapp.dto.project.ProjectDto;
import ar.edu.itba.paw.webapp.dto.project.ProjectStagesDto;
import ar.edu.itba.paw.webapp.dto.project.ProjectStatsDto;
import ar.edu.itba.paw.webapp.dto.project.UpdatableProjectDto;
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
import java.util.stream.Collectors;

@Path("projects")
@Component
public class ProjectRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectRestController.class);

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
    public Response create(@Valid final UpdatableProjectDto projectDto) {

        LOGGER.debug("Endpoint POST /projects reached with " + projectDto.toString() + " - User is " + sessionUser.getId());

        final List<Category> categories = projectDto.getCategories().stream().map(c -> new Category(c.getId())).collect(Collectors.toList());
        final Project project = projectService.create(projectDto.getName(), projectDto.getSummary(), projectDto.getFundingTarget(), categories, sessionUser.getId());
        final URI projectUri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(project.getId())).build();

        return Response.created(projectUri).header("Access-Control-Expose-Headers", "Location").build();
    }


    @GET
    @Path("/{id}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response project(@PathParam("id") long id) throws ProjectDoesNotExistException {

        LOGGER.debug("Endpoint GET /projects/" + id + " reached");

        final Project project = projectService.findById(id).orElseThrow(ProjectDoesNotExistException::new);
        final ProjectDto projectDto = ProjectDto.fromProject(project, uriInfo);
        if (!sessionUser.isAnonymous() && sessionUser.getId() == project.getOwnerId()) projectDto.setGetByOwner();
        return Response.ok(projectDto).build();
    }


    @PUT
    @Path("/{id}")
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response update(@PathParam("id") long id,
                           @Valid final UpdatableProjectDto projectDto) throws ProjectDoesNotExistException, IllegalProjectAccessException {

        LOGGER.debug("Endpoint PUT /projects/" + id + " reached with" + projectDto.toString() + " - User is " + sessionUser.getId());

        final List<Category> categories = projectDto.getCategories().stream().map(c -> new Category(c.getId())).collect(Collectors.toList());
        projectService.update(sessionUser.getId(), id, projectDto.getName(), projectDto.getSummary(),
                projectDto.getFundingTarget(), categories).orElseThrow(ProjectDoesNotExistException::new);
        return Response.ok().build();
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
                             @Min(0) @QueryParam("pmax") Double maxFundingPercentage,
                             @Min(0) @QueryParam("pmin") Double minFundingPercentage,
                             @QueryParam("l") @DefaultValue("3") int limit) {

        LOGGER.debug("Endpoint GET /projects reached");

        final Page<Project> projectPage = projectService.findAll(category, minFundingTarget, maxFundingTarget,
                minFundingPercentage, maxFundingPercentage, keyword, field, order, page, limit);
        projectPage.setPageRange(PAGINATION_ITEMS);

        final List<ProjectDto> projects = projectPage.getContent().stream().map(p -> ProjectDto.fromProject(p, uriInfo)).collect(Collectors.toList());

        return Response.ok(new GenericEntity<List<ProjectDto>>(projects) {})
                .link(uriInfo.getRequestUriBuilder().replaceQueryParam("p", 1).build(), "first")
                .link(uriInfo.getRequestUriBuilder().replaceQueryParam("p", projectPage.getStartPage()).build(), "start")
                .link(uriInfo.getRequestUriBuilder().replaceQueryParam("p", projectPage.getEndPage()).build(), "end")
                .link(uriInfo.getRequestUriBuilder().replaceQueryParam("p", projectPage.getTotalPages()).build(), "last")
                .header("Access-Control-Expose-Headers", "Link")
                .build();
    }

    /** Extra data for projects */

    @GET
    @Path("/{id}/categories")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response projectCategories(@PathParam("id") long id) throws ProjectDoesNotExistException {

        LOGGER.debug("Endpoint GET /projects/" + id + "/categories reached");

        final Project project = projectService.findById(id).orElseThrow(ProjectDoesNotExistException::new);
        final List<CategoryDto> categoriesDto = project.getCategories().stream().map(CategoryDto::fromCategory).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<CategoryDto>>(categoriesDto) {}).build();
    }


    // Recibe lista como -d'[{"id":14,"name":"3D_Printers","parent":1},...]'
    @PUT
    @Path("/{id}/categories")
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response updateCategories(@PathParam("id") long id,
                                     @NotEmpty final List<CategoryDto> categoriesDto) throws ProjectDoesNotExistException, IllegalProjectAccessException {

        LOGGER.debug("Endpoint PUT /projects/" + id + "/categories reached with" + categoriesDto.toString() + " - User is " + sessionUser.getId());

        final List<Category> categories = categoriesDto.stream().map(c -> new Category(c.getId())).collect(Collectors.toList());
        projectService.addCategories(sessionUser.getId(), id, categories).orElseThrow(ProjectDoesNotExistException::new);
        return Response.ok().build();
    }


    @GET
    @Path("/{id}/stats")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response getStats(@PathParam("id") long id) throws ProjectDoesNotExistException {

        LOGGER.debug("Endpoint GET /projects/" + id + "/stats reached");

        final Project project = projectService.getStats(id).orElseThrow(ProjectDoesNotExistException::new);
        return Response.ok(ProjectStatsDto.fromProjectStats(project.getStats())).build();
    }


    @PUT
    @Path("/{id}/stats")
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response setStats(@PathParam("id") long id,
                             @Valid final ProjectStatsDto statsDto) throws ProjectDoesNotExistException {

        LOGGER.debug("Endpoint PUT /projects/" + id + "/stats reached with" + statsDto.toString());

        projectService.addStats(id, statsDto.getSecondsAvg(), statsDto.getClicksAvg(), sessionUser.isInvestor(),
                statsDto.getContactClicks() >= 1).orElseThrow(ProjectDoesNotExistException::new);
        return Response.ok().build();
    }


    @GET
    @Path("/{id}/stages")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response getStages(@PathParam("id") long id) throws ProjectDoesNotExistException {

        LOGGER.debug("Endpoint GET /projects/" + id + "/stages reached");

        final Project project = projectService.findById(id).orElseThrow(ProjectDoesNotExistException::new);
        final List<ProjectStagesDto> stagesDto = project.getStages().stream().map(ProjectStagesDto::fromProjectStages).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<ProjectStagesDto>>(stagesDto) {}).build();
    }


    @PUT
    @Path("/{id}/stages")
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response addStages(@PathParam("id") long id,
                              @Valid final ProjectStagesDto stagesDto) throws ProjectDoesNotExistException, IllegalProjectAccessException {

        LOGGER.debug("Endpoint PUT /projects/" + id + "/stages reached with" + stagesDto.toString() + " - User is " + sessionUser.getId());

        projectService.setStage(sessionUser.getId(), id, stagesDto.getName(), stagesDto.getComment()).orElseThrow(ProjectDoesNotExistException::new);
        return Response.ok().build();
    }


    @PUT
    @Path("/{id}/close")
    public Response close(@PathParam("id") long id) throws ProjectDoesNotExistException, IllegalProjectAccessException {

        LOGGER.debug("Endpoint PUT /projects/" + id + "/close reached - User is " + sessionUser.getId());

        projectService.toggleClosed(sessionUser.getId(), id).orElseThrow(ProjectDoesNotExistException::new);
        return Response.ok().build();
    }


    /** Get all the available categories */

    @GET
    @Path("/categories")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response categories() {

        LOGGER.debug("Endpoint GET /projects/categories reached");

        final List<Category> categories = projectService.getAllCategories();
        final List<CategoryDto> categoriesDto = categories.stream().map(CategoryDto::fromCategory).collect(Collectors.toList());

        return Response.ok(new GenericEntity<List<CategoryDto>>(categoriesDto) {}).build();
    }
}
