package ar.edu.itba.paw.interfaces.daos;


import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.components.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Filter;

public interface ProjectDao {

    /**
     * Creates a project given its parameters.
     * @param name The project's name.
     * @param summary The project's summary.
     * @param cost The project's total cost.
     * @param owner The user owner of the project.
     * @param categories The project's categories.
     * @param image The project's front image.
     * @return The created project.
     */
    Project create(String name, String summary, long cost, byte[] image, User owner, List<Category> categories);


    /**
     * Finds a project given its id
     * @param id The unique id for the project
     * @return The matched project or null otherwise
     */
    Optional<Project> findById(long id);


    /**
     * Finds all projects with the given filter.
     * @param filters All the filters applied to the search.
     * @param order The order field to order by.
     * @param page The page requested.
     * @return The list of matching projects.
     */
    Page<Project> findAll(List<FilterCriteria> filters, OrderField order, PageRequest page);


    /**
     * Finds all the projects given the filters, ordered not paged.
     * @param filters Filters to be applied to the projects.
     * @param order Order to bring the projects.
     * @return List with the projects.
     */
    List<Project> findAll(List<FilterCriteria> filters, OrderField order);


    /**
     * Finds all the possible categories from the database.
     * @return List of all the categories.
     */
    List<Category> findAllCategories();
}
