package ar.edu.itba.paw.interfaces.daos;


import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.components.FilterCriteria;
import ar.edu.itba.paw.model.components.OrderField;
import ar.edu.itba.paw.model.components.Pair;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
     * Finds the projects owned by the same user.
     * @param owner The user owner.
     * @return List of all the project for the given user.
     */
    List<Project> findByOwner(User owner);

    /**
     * Finds all projects with the given filter.
     * @param params All the filters applied to the search.
     * @param order The order field to order by.
     * @return The list of matching projects.
     */
    List<Project> findAll(List<FilterCriteria> params, OrderField order);








    /**
     * Finds a project given its id
     * @param projectId The unique id for the project
     * @return The matched project or null otherwise
     */
    Optional<Project> findById(long projectId);

    /**
     * Finds all the projects given a list of ids.
     * @param ids The list of projects id.
     * @return The list with containing projects.
     */
    List<Project> findByIds(List<Long> ids);





    /**
     * Counts all projects with the given filter.
     * @param filter All the filters applied to the search.
     * @return The quantity of matching projects.
     */
//    Integer countFiltered(Ls filter);

    /**
     * @param projectId The id of the project we want to get a portrait image
     * @return Image as a byte array
     */
    byte[] findImageForProject(long projectId);

    /**
     * Adds a hit to the given project.
     * @param projectId The unique project id.
     */
    void addHit(long projectId);

    /**
     * Adds to the user the project as favorite.
     * @param projectId The unique project id.
     * @param userId The unique user id.
     */
    void addFavorite(long projectId, long userId);

    /**
     * Deletes a project as favorite for the given user.
     * @param projectId The unique project id.
     * @param userId The unique user id.
     */
    void deleteFavorite(long projectId, long userId);

    /**
     * Finds if a user has a given project as favorite.
     * @param projectId The unique project id.
     * @param userId The unique user id.
     * @return If user has it as favorite true, false otherwise.
     */
    boolean isFavorite(long projectId, long userId);

    /**
     * Find all projects matching with user id.
     * @return List of Projects.
     */
    List<Long> findFavorites(long id);

    /**
     * Gets the count of how many times the given project is favorite.
     * @param projectId The unique project id.
     * @return Count of favorites.
     */
    long getFavoritesCount(long projectId);

    /**
     * Gets the count of how many times each given project is favorite.
     * @param projectIds The list of project ids.
     * @return Count of favorites for each id ordered by Id.
     */
    List<Long> getFavoritesCount(List<Long> projectIds);

    /**
     * @param projectIds projects to check if where faved by user
     * @param userId user who could have faved ids
     * @return boolean list whether project id was faved or not
     */
    List<Boolean> isFavorite(List<Long> projectIds, long userId);
}
