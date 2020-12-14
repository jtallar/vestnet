package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.components.Page;
import ar.edu.itba.paw.model.image.ProjectImage;

import java.util.List;
import java.util.Optional;

public interface ProjectService {

    /**
     * Creates a project given its parameters.
     * @param name The project's name.
     * @param summary The project's summary.
     * @param cost The project's total cost.
     * @param ownerId The user id owner of the project.
     * @return operation return.
     */
    Project create(String name, String summary, long cost, long ownerId);


    /**
     * Updates a project with the given parameters.
     * @param ownerId The owner's unique id.
     * @param id Project's unique id.
     * @param name The project's name.
     * @param summary The project's summary.
     * @param cost The project's total cost.
     * @return operation return.
     * @return
     */
    Optional<Project> update(long ownerId, long id, String name, String summary, long cost);


    /**
     * Finds a project given its id
     * @param id The unique id for the project
     * @return The matched project or null otherwise
     */
    Optional<Project> findById(long id);


    /**
     * Finds all projects with the given filter.
     * @param category Projects category. Null if no category set.
     * @param minCost Projects min cost. Null if no min cost set.
     * @param maxCost Projects max cost. Null if no max cost set.
     * @param keyword Keyword to be matched on search. Null or "" if no search.
     * @param field The field to search the keyword.
     * @param order The order to order by.
     * @param page The number of page to get the projects.
     * @param pageSize The page size to consider.
     * @return The list of matching projects.
     */
    Page<Project> findAll(Integer category, Integer minCost, Integer maxCost, String keyword, int field, int order, int page, int pageSize);


    /**
     * Adds a hit to the given project.
     * @param id The unique project id.
     * @return The modified optional project.
     */
    Optional<Project> addHit(long id);


    /**
     * Sets a project as funded.
     * @param ownerId The owner's unique id.
     * @param id The unique project id.
     * @return The optional project modified.
     */
    Optional<Project> setFunded(long ownerId, long id);


    /**
     * Replaces the projects categories with the given ones.
     * @param ownerId The owner's unique id.
     * @param id The unique project's id.
     * @param categories The list of categories.
     * @return The modified project if found.
     */
    Optional<Project> addCategories(long ownerId, long id, List<Category> categories);


    /**
     * Sets the project portrait image.
     * @param ownerId The owner's unique id.
     * @param id The unique project id.
     * @param image The image to set.
     * @return The optional modified project.
     */
    Optional<Project> setPortraitImage(long ownerId, long id, byte [] image);


    /**
     * Sets the project slideshow images.
     * @param ownerId The owner's unique id.
     * @param id The unique project id.
     * @param images The images to insert.
     * @return The optional modified project.
     */
    Optional<Project> setSlideshowImages(long ownerId, long id, List<byte []> images);


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
    Optional<ProjectImage> getPortraitImage(long id);


    /**
     * Finds all slideshow images.
     * @param id The unique project id to find its images.
     * @return A list with all the related images.
     */
    List<ProjectImage> getSlideshowImages(long id);
}
