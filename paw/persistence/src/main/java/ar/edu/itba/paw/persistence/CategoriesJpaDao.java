package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.CategoriesDao;
import ar.edu.itba.paw.model.Category;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class CategoriesJpaDao implements CategoriesDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Cacheable("allCategories")
    public List<Category> findAll() {
        final TypedQuery<Category> query = entityManager.createQuery("from Category order by name", Category.class);
        return query.getResultList();
    }

    @Override
    public List<Category> findProjectCategories(long projectId) {
        return null;
    }
}
