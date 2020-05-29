package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.daos.CategoriesDao;
import ar.edu.itba.paw.interfaces.services.CategoriesService;
import ar.edu.itba.paw.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Primary
@Service
public class CategoriesServiceImpl implements CategoriesService {

    @Autowired
    CategoriesDao categoriesDao;

    @Override
    public List<Category> findAll() {
        return categoriesDao.findAll();
    }

    @Override
    public List<Category> findProjectCategories(long projectId) {
        return categoriesDao.findProjectCategories(projectId);
    }
}
