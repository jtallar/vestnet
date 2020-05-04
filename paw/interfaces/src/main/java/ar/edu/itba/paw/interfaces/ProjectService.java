package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ProjectService {

    /**
     * Finds a project given its id
     * @param id The unique id for the project
     * @return The matched project or null otherwise
     */
    Optional<Project> findById(long id);

    /**
     * Find all available projects
     * @return List of available projects
     */
    List<Project> findAll();

    List<Project> findByOwner(long userId);

    /**
     * Finds a list of projects that matches one or more categories
     * @param categories The list of categories to find
     * @return List of available projects that fit those categories
     */
    List<Project> findByCategories(List<Category> categories);

    public List<Project> findCoincidence(String name);

    /**
     * Create a project given all thes parameters
     * @return The created project
     */
//    Project create(String name, String summary, Date publishDate, Date updateDate, long cost, User owner,
//                   List<Category> categories, List<Stage> stages);

    // TODO: VER SI HACE FALTA DEVOLVER UN PROJECT O PUEDO DEVOLVER EL ID
    long create(String name, String summary, long cost, long ownerId, List<Long> categoriesIds, List<Stage> stages, byte[] imageBytes);

    /**
     * @param projectId The id of the project we want to get a portrait image
     * @return Image as a byte array
     */
    byte[] findImageForProject(long projectId);

    Integer projectsCount();

    List<Project> findCatForPage(List<Category> categories, int from, int to);

    Integer catProjCount(List<Category> categories);


    List<Project> findPage(int from, int to);

}
