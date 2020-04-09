package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.Category;

import java.util.List;

public interface CategoriesDao {

    List<Category> findAllCats();
}
