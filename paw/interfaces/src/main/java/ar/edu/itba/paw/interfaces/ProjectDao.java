package ar.edu.itba.paw.interfaces;


import ar.edu.itba.paw.model.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ProjectDao {

    /**
     * Finds a project given its id
     * @param id The unique id for the project
     * @return The matched project or null otherwise
     */
    Optional<Project> findById(long id);


    List<Project> findByOwner(long userId);

    public List<Project> findCoincidence(String name);

    /**
     * Find all available projects
     * @return List of available projects
     */
    List<Project> findAll();

    /**
     * Finds a list of projects that matches one or more categories
     * @param categories The list of categories to find
     * @return List of available projects that fit those categories
     */
    List<Project> findByCategories(List<Category> categories);

    /**
     * Create a project given all thes parameters
     * @return The created project
     */
//    Project create(String name, String summary, Date publishDate, Date updateDate, long cost, User owner,
//                   List<Category> categories, List<Stage> stages);
    // TODO: VER SI HACE FALTA DEVOLVER UN PROJECT O PUEDO DEVOLVER EL ID
    long create(String name, String summary, long cost, long ownerId, List<Long> categoriesIds, List<Stage> stages, byte[] imageBytes);




    List<Project> findPage(int from, int to);

    Integer projectsCount();

    List<Project> findCatForPage(List<Category> categories, int from, int to, long min, long max);

    Integer catProjCount(List<Category> categories);

    /**
     * @param projectId The id of the project we want to get a portrait image
     * @return Image as a byte array
     */
    byte[] findImageForProject(long projectId);

    /**
     * Find all projects matching with user id
     * @return list of Projects
     */
    List<Long>  findFavorites(long id);



    void addFavorite(long projectId, long userId);
    void deleteFavorite(long projectId, long userId);
    boolean isFavorite(long projectId, long userId);
}
