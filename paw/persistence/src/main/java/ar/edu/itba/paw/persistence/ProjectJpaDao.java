package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.ProjectDao;
import ar.edu.itba.paw.model.Category;
import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.components.FilterCriteria;
import ar.edu.itba.paw.model.components.OrderField;
import ar.edu.itba.paw.model.components.Page;
import ar.edu.itba.paw.model.components.PageRequest;
import org.springframework.cache.annotation.Cacheable;
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
        return Optional.ofNullable(entityManager.find(Project.class, id));
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
    public void addHit(long id) {
        Project project = entityManager.find(Project.class, id);
        project.setHits(project.getHits() + 1);
        entityManager.persist(project);
    }

    @Override
    @Cacheable("allCategories")
    public List<Category> findAllCategories() {
        final TypedQuery<Category> query = entityManager.createQuery("from Category order by name", Category.class);
        return query.getResultList();
    }



    /**
     * Auxiliary functions.
     */


    /**
     * Finds all the projects in the ids list, ordered.
     * @param ids The projects ids.
     * @param order The given order to order by.
     * @return List of projects.
     */
    private List<Project> findAllByIds(List<Long> ids, OrderField order) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Project> query = builder.createQuery(Project.class);

        Root<Project> root = query.from(Project.class);
        addPredicates(query, builder, root, Collections.singletonList(new FilterCriteria("ids", ids)));
        addOrder(query, builder, root, order);
        return entityManager.createQuery(query).getResultList();
    }

    /**
     * Counts how many projects match criteria.
     * @param filters Filter criteria to apply.
     * @return Count.
     */
    private Long findAllIdsCount(List<FilterCriteria> filters) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);

        Root<Project> root = query.from(Project.class);
        query.select(builder.countDistinct(root.get("id")));
        addPredicates(query, builder,root, filters);
        return entityManager.createQuery(query).getSingleResult();
    }


    /**
     * Finds all ids of projects given a filter, order and page.
     * @param filters The filters to apply.
     * @param order The order to order by.
     * @param page The page requested.
     * @return List of all unique ids.
     */
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
