package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.interfaces.exceptions.IllegalProjectAccessException;
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
     * @param fundingTarget The project's total funding target.
     * @param categories The project's categories
     * @param ownerId The user id owner of the project.
     * @return operation return.
     */
    Project create(String name, String summary, long fundingTarget, List<Category> categories, long ownerId);


    /**
     * Updates a project with the given parameters.
     * @param ownerId The owner's unique id.
     * @param id Project's unique id.
     * @param name The project's name.
     * @param summary The project's summary.
     * @param fundingTarget The project's total funding target.
     * @param categories The project's categories.
     * @return operation return.
     * @throws IllegalProjectAccessException If the user attempting to update the project is not its owner.
     */
    Optional<Project> update(long ownerId, long id, String name, String summary, long fundingTarget, List<Category> categories) throws IllegalProjectAccessException;


    /**
     * Finds a project given its id
     * @param id The unique id for the project
     * @return The matched project or null otherwise
     */
    Optional<Project> findById(long id);


    /**
     * Finds all projects with the given filter.
     * @param category Projects category. Null if no category set.
     * @param minFundingTarget Projects min funding target. Null if empty.
     * @param maxFundingTarget Projects max funding target. Null if empty.
     * @param minFundingPercentage The min percentage of founded. Null if none.
     * @param maxFundingPercentage The max percentage of founded. Null if none.
     * @param keyword Keyword to be matched on search. Null or "" if no search.
     * @param field The field to search the keyword.
     * @param order The order to order by.
     * @param page The number of page to get the projects.
     * @param pageSize The page size to consider.
     * @return The list of matching projects.
     */
    Page<Project> findAll(Integer category, Integer minFundingTarget, Integer maxFundingTarget, Double minFundingPercentage,
                          Double maxFundingPercentage, String keyword, int field, int order, int page, int pageSize);


    /**
     * Toggles the project to open or closed.
     * @param ownerId The owner's unique id.
     * @param id The unique project id.
     * @return The optional project modified.
     * @throws IllegalProjectAccessException If the user attempting to update the project is not its owner.
     */
    Optional<Project> toggleClosed(long ownerId, long id) throws IllegalProjectAccessException;


    /**
     * Replaces the projects categories with the given ones.
     * @param ownerId The owner's unique id.
     * @param id The unique project's id.
     * @param categories The list of categories.
     * @return The modified project if found.
     * @throws IllegalProjectAccessException If the user attempting to update the project is not its owner.
     */
    Optional<Project> addCategories(long ownerId, long id, List<Category> categories) throws IllegalProjectAccessException;


    /**
     * Gets the stats of the project.
     * @param id The unique project's id.
     * @return
     */
    Optional<Project> getStats(long id);


    /**
     * Updates the stats of the project given its received values.
     * @param id The unique project's id.
     * @param seconds The seconds the user spent on the page.
     * @param clicks The amount of clicks made on the page.
     * @param investor If the user was an investor.
     * @param contact If the user pressed the contact button.
     * @return
     */
    Optional<Project> addStats(long id, long seconds, long clicks, boolean investor, boolean contact);


    /**
     * Set the last not completed stage as completed
     * @param ownerId The owner's unique id.
     * @param id The unique project's id.
     * @param name The name of the the stage completed.
     * @param comment The comment on the new completed stage.
     * @return The optional project, modified if found.
     * @throws IllegalProjectAccessException If the user attempting to update the project is not its owner.
     */
    Optional<Project> setStage(long ownerId, long id, String name, String comment) throws IllegalProjectAccessException;


    /**
     * Finds project main profile image.
     * @param id The unique project id to find its image.
     * @return The main image or default if none is found.
     */
    Optional<ProjectImage> getPortraitImage(long id);


    /**
     * Sets the project portrait image.
     * @param ownerId The owner's unique id.
     * @param id The unique project id.
     * @param image The image to set.
     * @return The optional modified project.
     * @throws IllegalProjectAccessException If the user attempting to update the project is not its owner.
     */
    Optional<Project> setPortraitImage(long ownerId, long id, byte [] image) throws IllegalProjectAccessException;


    /**
     * Finds all slideshow images.
     * @param id The unique project id to find its images.
     * @return A list with all the related images.
     */
    List<ProjectImage> getSlideshowImages(long id);


    /**
     * Sets the project slideshow images.
     * @param ownerId The owner's unique id.
     * @param id The unique project id.
     * @param images The images to insert.
     * @return The optional modified project.
     * @throws IllegalProjectAccessException If the user attempting to update the project is not its owner.
     */
    Optional<Project> setSlideshowImages(long ownerId, long id, List<byte []> images) throws IllegalProjectAccessException;


    /**
     * Finds all the possible categories from the database.
     * @return List of all the categories.
     */
    List<Category> getAllCategories();
}
