package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.components.Page;
import ar.edu.itba.paw.model.image.ProjectImage;

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
     * @param image Project portrait image.
     * @param slideshow Project slideshow images.
     * @return operation return.
     */
    Project create(String name, String summary, long cost, long ownerId, List<Long> categoriesIds, byte[] image, List<byte[]> slideshow);


    /**
     * Finds a project given its id
     * @param id The unique id for the project
     * @return The matched project or null otherwise
     */
    Optional<Project> findById(long id);


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
     * Sets a project as funded.
     * @param projectId The unique project id.
     * @return The project modified, null if not found.
     */
    Project setFunded(long projectId);


    /**
     * Ads one more message to the project's message count.
     * @param projectId The unique project id.
     * @return The updated project.
     */
    Project addMsgCount(long projectId);


    /**
     * Removes one message to the project's message count.
     * @param projectId The unique project id.
     * @return The updated project.
     */
    Project decMsgCount(long projectId);


    /**
     * Finds all the possible categories from the database.
     * @return List of all the categories.
     */
    List<Category> getAllCategories();


    /**
     * Finds project main profile image.
     * @param id The unique project id to find its image.
     * @return The main image or default if none is found.
     */
    byte[] getPortraitImage(long id);


    /**
     * Finds all slideshow images.
     * @param id The unique project id to find its images.
     * @return A list with all the related images.
     */
    List<byte[]> getSlideshowImages(long id);
}
