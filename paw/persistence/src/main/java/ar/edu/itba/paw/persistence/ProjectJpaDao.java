package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.ProjectDao;
import ar.edu.itba.paw.model.Category;
import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.components.FilterCriteria;
import ar.edu.itba.paw.model.components.OrderField;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.Optional;

@Repository
public class ProjectJpaDao implements ProjectDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Project create(String name, String summary, long cost, byte[] image, User owner, List<Category> categories) {
        final Project project = new Project(name, summary, cost, image, owner, categories);
        entityManager.persist(project);
        return project;
    }

    @Override
    public List<Project> findByOwner(User owner) {
        final TypedQuery<Project> query = entityManager.createQuery("from Project where owner = :owner", Project.class);
        query.setParameter("owner", owner);
        return query.getResultList();
    }

    @Override
    public List<Project> findAll(List<FilterCriteria> params, OrderField order) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Project> query = builder.createQuery(Project.class);
        Root<Project> root = query.from(Project.class);

        addPredicates(query, builder, root, params);
        addOrder(query, builder, root, order);
        return entityManager.createQuery(query).getResultList();
    }


    private CriteriaQuery<Project> addOrder(CriteriaQuery<Project> query, CriteriaBuilder builder, Root<Project> root, OrderField order) {
        switch (order) {
            case DEFAULT:  query.orderBy(builder.desc(root.get("hits")), builder.desc(root.get("id"))); break;
            case ALPHABETICAL: query.orderBy(builder.asc(root.get("name")), builder.desc(root.get("id"))); break;
            case COST_ASCENDING: query.orderBy(builder.asc(root.get("cost")), builder.desc(root.get("id"))); break;
            case COST_DESCENDING: query.orderBy(builder.desc(root.get("cost")), builder.desc(root.get("id"))); break;
            case DATE_ASCENDING: query.orderBy(builder.asc(root.get("publishDate")), builder.desc(root.get("id"))); break;
            case DATE_DESCENDING: query.orderBy(builder.desc(root.get("publishDate")), builder.desc(root.get("id"))); break;
        }
        return query;
    }

    private CriteriaQuery<Project> addPredicates(CriteriaQuery<Project> query, CriteriaBuilder builder, Root<Project> root, List<FilterCriteria> params) {
        Predicate predicate = builder.conjunction();
        ProjectCriteriaConsumer filterConsumer = new ProjectCriteriaConsumer(predicate, builder, root);
        params.stream().forEach(filterConsumer);
        predicate = filterConsumer.getPredicate();
        query.where(predicate);
        return query;
    }







    @Override
    public Optional<Project> findById(long projectId) {

        return Optional.empty();
    }

    @Override
    public List<Project> findByIds(List<Long> ids) {
        return null;
    }


//    @Override
//    public Integer countFiltered(ProjectFilter filter) {
//        return null;
//    }

    @Override
    public byte[] findImageForProject(long projectId) {
        return new byte[0];
    }

    @Override
    public void addHit(long projectId) {

    }

    @Override
    public void addFavorite(long projectId, long userId) {

    }

    @Override
    public void deleteFavorite(long projectId, long userId) {

    }

    @Override
    public boolean isFavorite(long projectId, long userId) {
        return false;
    }

    @Override
    public List<Long> findFavorites(long id) {
        return null;
    }

    @Override
    public long getFavoritesCount(long projectId) {
        return 0;
    }

    @Override
    public List<Long> getFavoritesCount(List<Long> projectIds) {
        return null;
    }

    @Override
    public List<Boolean> isFavorite(List<Long> projectIds, long userId) {
        return null;
    }





}
