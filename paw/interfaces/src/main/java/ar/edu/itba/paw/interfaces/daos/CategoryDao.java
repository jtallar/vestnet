package ar.edu.itba.paw.interfaces.daos;

import ar.edu.itba.paw.model.Category;

import java.util.List;

public interface CategoryDao {

    /**
     * Finds all the possible categories from the database.
     * @return List of all the categories.
     */
    List<Category> findAllCategories();
}
