package ar.edu.itba.paw.interfaces;


import ar.edu.itba.paw.model.*;

import java.util.List;
import java.util.Optional;

public interface ProjectDao {

    /**
     * Creates a project given its parameters.
     * @param name The project's name.
     * @param summary The project's summary.
     * @param cost The project's total cost.
     * @param ownerId The user id owner of the project.
     * @param categoriesIds The project's categories ids.
     * @param stages The project's list of stages.
     * @param imageBytes The project's front image.
     * @return operation return.
     */
    long create(String name, String summary, long cost, long ownerId, List<Long> categoriesIds, List<Stage> stages, byte[] imageBytes);

    /**
     * Finds a project given its id
     * @param projectId The unique id for the project
     * @return The matched project or null otherwise
     */
    Optional<Project> findById(long projectId);

    /**
     * Finds the projects owned by the same user.
     * @param userId Unique user id.
     * @return List of all the project for the given user.
     */
    List<Project> findByOwner(long userId);

    /**
     * Gets the count of all the published projects within a cost range.
     * @param minCost The minimum cost for the project.
     * @param maxCost The maximum cost for the project.
     * @return The count of projects that match criteria.
     */
    Integer countByCost(long minCost, long maxCost);

    /**
     * Finds all the matches for a name given its selection.
     * @param name String to search matches for.
     * @param selection Specific field searched for a match.
     * @param pageStart Starting page.
     * @param pageOffset Page offset.
     * @return List of projects that meet the criteria.
     */
    List<Project> findByCoincidencePage(String name, String selection, int pageStart, int pageOffset);

    /**
     * Finds how many projects match with the criteria.
     * @param name The string to search matches for.
     * @param selection Specific field searched for a match.
     * @return Project count.
     */
    Integer countByCoincidence(String name, String selection);

    /**
     * Finds all projects with any of the given categories within the cost range.
     * @param categories The list of possible categories.
     * @param pageStart Starting page.
     * @param pageOffset Page offset.
     * @param minCost Minimum cost.
     * @param maxCost Maximum cost.
     * @return List of projects.
     */
    List<Project> findByCategoryPage(List<Category> categories, int pageStart, int pageOffset, long minCost, long maxCost);

    /**
     * Finds how many projects with any of the given categories within the cost range.
     * @param categories The list of possible categories.
     * @param minCost Minimum cost.
     * @param maxCost Maximum cost.
     * @return Count of the projects that meet the given criteria.
     */
    Integer countByCategory(List<Category> categories, long minCost, long maxCost);

    /**
     * Finds all the projects within a cost range.
     * @param pageStart Starting page.
     * @param pageOffset Page offset.
     * @param minCost Minimum cost.
     * @param maxCost Maximum cost.
     * @return List of found projects.
     */
    List<Project> findByCostPage(int pageStart, int pageOffset, long minCost, long maxCost);

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
     * @param projectIds projects to check if where faved by user
     * @param userId user who could have faved ids
     * @return boolean list whether project id was faved or not
     */
    List<Boolean> isFavorite(List<Long> projectIds, long userId);
}
