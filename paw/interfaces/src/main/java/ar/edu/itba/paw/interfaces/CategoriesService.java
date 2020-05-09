package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.Category;

import java.util.List;

public interface CategoriesService {

    /**
     * Finds all the possible categories from the database.
     * @return List of all the categories.
     */
    List<Category> findAllCats();

    /**
     * Finds all categories for a given project.
     * @param projectId The unique project id.
     * @return List of all the categories of the given project.
     */
    List<Category> findProjectCategories(long projectId);
}
