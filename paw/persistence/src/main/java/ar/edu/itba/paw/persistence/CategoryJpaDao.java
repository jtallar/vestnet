package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.CategoryDao;
import ar.edu.itba.paw.model.Category;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class CategoryJpaDao implements CategoryDao {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    @Cacheable("allCategories")
    public List<Category> findAllCategories() {
        final TypedQuery<Category> query = entityManager.createQuery("from Category order by name", Category.class);
        return query.getResultList();
    }
}
