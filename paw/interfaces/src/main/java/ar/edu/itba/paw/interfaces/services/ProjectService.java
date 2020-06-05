package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.components.Page;
import ar.edu.itba.paw.model.components.Pair;
import ar.edu.itba.paw.model.components.SearchField;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProjectService {

    /**
     * Creates a project given its parameters.
     * @param name The project's name.
     * @param summary The project's summary.
     * @param cost The project's total cost.
     * @param ownerId The user id owner of the project.
     * @param categoriesIds The project's categories ids.
     * @return operation return.
     */
    Project create(String name, String summary, long cost, long ownerId, List<Long> categoriesIds);

    /**
     * Finds a project given its id
     * @param id The unique id for the project
     * @return The matched project or null otherwise
     */
    Optional<Project> findById(long id);

    /**
     * Finds the projects owned by the same user.
     * @param id Unique user id.
     * @return List of all the project for the given user.
     */
    List<Project> findByOwnerId(long id);

    /**
     * Finds all projects with the given filter.
     * @param filters All the filters applied to the search.
     * @param order The order to order by.
     * @param page The number of page to get the projects.
     * @param pageSize The page size to consider.
     * @return The list of matching projects.
     */
    Page<Project> findAll(Map<String, Object> filters, String order, Integer page, Integer pageSize);

    /**
     * Adds a hit to the given project.
     * @param projectId The unique project id.
     * @return The modified project, null if not found.
     */
    Project addHit(long projectId);

    /**
     * Finds all the possible categories from the database.
     * @return List of all the categories.
     */
    List<Category> findAllCategories();
}
