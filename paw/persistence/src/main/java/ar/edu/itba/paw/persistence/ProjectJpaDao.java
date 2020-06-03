package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.ProjectDao;
import ar.edu.itba.paw.model.Category;
import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.components.FilterCriteria;
import ar.edu.itba.paw.model.components.OrderField;
import ar.edu.itba.paw.model.components.Page;
import ar.edu.itba.paw.model.components.PageRequest;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Collections;
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
    public Optional<Project> findById(long id) {
        final TypedQuery<Project> query = entityManager.createQuery("from Project where id = :id", Project.class);
        query.setParameter("id", id);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Page<Project> findAll(List<FilterCriteria> filters, OrderField order, PageRequest page) {
        /** Get to total count of projects with matching criteria */
        Long count = findAllIdsCount(filters);
        if (count == 0) return new Page<>(new ArrayList<>(), page.getPage(), page.getPageSize(), count);

        /** Get all project's ids with matching criteria, order and page  */
        // TODO check here, should be an exception. Wrong page
        List<Long> ids = findAllIds(filters, order, page);
        if (ids.isEmpty()) return new Page<>(new ArrayList<>(), page.getPage(), page.getPageSize(), count);

        /** Gets all the corresponding projects in order */
        List<Project> projects = findAllByIds(ids, order);

        return new Page<>(projects, page.getPage(), page.getPageSize(), count);
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


    /**
     * Auxiliary functions.
     */


    private List<Project> findAllByIds(List<Long> ids, OrderField order) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Project> query = builder.createQuery(Project.class);

        Root<Project> root = query.from(Project.class);
        addPredicates(query, builder, root, Collections.singletonList(new FilterCriteria("ids", ids)));
        addOrder(query, builder, root, order);
        return entityManager.createQuery(query).getResultList();
    }

    private Long findAllIdsCount(List<FilterCriteria> filters) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);

        Root<Project> root = query.from(Project.class);
        query.select(builder.countDistinct(root.get("id")));
        addPredicates(query, builder,root, filters);
        return entityManager.createQuery(query).getSingleResult();
    }

    private List<Long> findAllIds(List<FilterCriteria> filters, OrderField order, PageRequest page) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);

        Root<Project> root = query.from(Project.class);
        query.select(root.get("id"));
        addPredicates(query, builder, root, filters);
        addOrder(query, builder, root, order);
        query.groupBy(root.get("id"));

        TypedQuery<Long> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult(page.getFirstResult());
        typedQuery.setMaxResults(page.getPageSize());
        return typedQuery.getResultList();
    }



    /**
     * Applies to the query the given order.
     * @param <T> The type of criteria query.
     * @param query The query to be applied the order.
     * @param builder Criteria query builder;
     * @param root Query project root.
     * @param order The order to be applied.
     */
    private <T> void addOrder(CriteriaQuery<T> query, CriteriaBuilder builder, Root<Project> root, OrderField order) {
        switch (order) {
            case DEFAULT:  query.orderBy(builder.desc(root.get("hits")), builder.desc(root.get("id"))); break;
            case ALPHABETICAL: query.orderBy(builder.asc(root.get("name")), builder.desc(root.get("id"))); break;
            case COST_ASCENDING: query.orderBy(builder.asc(root.get("cost")), builder.desc(root.get("id"))); break;
            case COST_DESCENDING: query.orderBy(builder.desc(root.get("cost")), builder.desc(root.get("id"))); break;
            case DATE_ASCENDING: query.orderBy(builder.asc(root.get("publishDate")), builder.desc(root.get("id"))); break;
            case DATE_DESCENDING: query.orderBy(builder.desc(root.get("publishDate")), builder.desc(root.get("id"))); break;
        }
    }

    /**
     * Applies the given filters to the query.
     * @param <T> The type of Criteria Query
     * @param query The query to be applied the filters.
     * @param builder Criteria query builder.
     * @param root Query project root.
     * @param filters List of criteria filters.
     */
    private <T> void addPredicates(CriteriaQuery<T> query, CriteriaBuilder builder, Root<Project> root, List<FilterCriteria> filters) {
        Predicate predicate = builder.conjunction();
        ProjectCriteriaConsumer filterConsumer = new ProjectCriteriaConsumer(predicate, builder, root);
        filters.stream().forEach(filterConsumer);
        predicate = filterConsumer.getPredicate();
        query.where(predicate);
    }
}
