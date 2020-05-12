package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.components.ProjectFilter;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ProjectService {

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
    @Transactional
    long create(String name, String summary, long cost, long ownerId, List<Long> categoriesIds, List<Stage> stages, byte[] imageBytes);

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
     * Finds the projects owned by the same user.
     * @param userId Unique user id.
     * @return List of all the project for the given user.
     */
    List<Project> findByOwner(long userId);

    /**
     * Finds all projects with the given filter.
     * @param filter All the filters applied to the search.
     * @return The list of matching projects.
     */
    List<Project> findFiltered(ProjectFilter filter);

    /**
     * Counts all projects with the given filter.
     * @param filter All the filters applied to the search.
     * @return The quantity of matching projects.
     */
    Integer countFiltered(ProjectFilter filter);

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

    /**
     * Gets a list of all the user favorite projects.
     * @param userId The unique user id.
     * @return The list of favorited projects.
     */
     List<Project> getUserFavorites(long userId);
}
