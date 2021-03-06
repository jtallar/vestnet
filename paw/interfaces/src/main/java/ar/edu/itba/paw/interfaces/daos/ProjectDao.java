package ar.edu.itba.paw.interfaces.daos;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.components.*;

import java.util.List;
import java.util.Optional;

public interface ProjectDao {

    /**
     * Creates a project given its parameters.
     * @param name The project's name.
     * @param summary The project's summary.
     * @param fundingTarget The project's total funding target.
     * @param owner The user owner of the project.
     * @return The created project.
     */
    Project create(String name, String summary, long fundingTarget, User owner);


    /**
     * Finds a project given its id
     * @param id The unique id for the project
     * @return The matched project or null otherwise
     */
    Optional<Project> findById(long id);


    /**
     * Finds all projects with the given filter.
     * @param request The builder with the parameters of the search.
     * @param page Page request, with page number and size.
     * @return The list of matching projects.
     */
    Page<Project> findAll(RequestBuilder request, PageRequest page);


    /**
     * Finds all the projects given the filters, ordered not paged.
     * @param request The builder with the parameters of the search.
     * @return List with the projects.
     */
    List<Project> findAll(RequestBuilder request);
}
