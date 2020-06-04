package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.MessageDao;
import ar.edu.itba.paw.interfaces.exceptions.MessageAlreadySentException;
import ar.edu.itba.paw.model.Message;
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
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class MessageJpaDao implements MessageDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Message create(Message.MessageContent content, User sender, User receiver, Project project) throws MessageAlreadySentException {
        // TODO check this how are we going to do it
        if(project == null) throw new MessageAlreadySentException();

        final Message message = new Message(content, sender, receiver, project);
        entityManager.persist(message);
        return message;
    }

    @Override
    public Page<Message> findAll(List<FilterCriteria> filters, OrderField order, PageRequest page) {
        /** Get to total count of messages with matching criteria */
        Long count = findAllIdsCount(filters);
        if (count == 0) return new Page<>(new ArrayList<>(), page.getPage(), page.getPageSize(), count);

        /** Get all messages's ids with matching criteria, order and page  */
        // TODO check here, should be an exception. Wrong page
        List<Long> ids = findAllIds(filters, order, page);
        if (ids.isEmpty()) return new Page<>(new ArrayList<>(), page.getPage(), page.getPageSize(), count);

        /** Gets all the corresponding messages in order */
        List<Message> messages = findAllByIds(ids, order);

        return new Page<>(messages, page.getPage(), page.getPageSize(), count);
    }

    @Override
    public List<Message> findAll(List<FilterCriteria> filters, OrderField order) {
        /** Finds all avoiding paging and thus 2 unnecessary queries */
        return findAllNotPaged(filters, order);
    }

    @Override
    public Message updateMessageStatus(List<FilterCriteria> filters, boolean accepted) {
        // TODO maybe throw no such element exception?
        Optional<Message> optionalMessage = findAll(filters, OrderField.DEFAULT).stream().findFirst();
        if (!optionalMessage.isPresent()) return null;
        Message message = optionalMessage.get();
        message.setAccepted(accepted);
        entityManager.persist(message);
        return message;
    }

    /**
     * Auxiliary functions.
     */


    /**
     * Finds all ids of messages given a filter and order.
     * @param filters The filters to apply.
     * @param order The order to order by.
     * @return List of all unique ids.
     */
    private List<Message> findAllNotPaged(List<FilterCriteria> filters, OrderField order) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Message> query = builder.createQuery(Message.class);

        Root<Message> root = query.from(Message.class);
        addPredicates(query, builder, root, filters);
        addOrder(query, builder, root, order);

        return entityManager.createQuery(query).getResultList();
    }


    /**
     * Finds all the projects in the ids list, ordered.
     * @param ids The projects ids.
     * @param order The given order to order by.
     * @return List of projects.
     */
    private List<Message> findAllByIds(List<Long> ids, OrderField order) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Message> query = builder.createQuery(Message.class);

        Root<Message> root = query.from(Message.class);
        addPredicates(query, builder, root, Collections.singletonList(new FilterCriteria("ids", ids)));
        addOrder(query, builder, root, order);
        return entityManager.createQuery(query).getResultList();
    }


    /**
     * Counts how many messages match criteria.
     * @param filters Filter criteria to apply.
     * @return Count.
     */
    private Long findAllIdsCount(List<FilterCriteria> filters) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);

        Root<Message> root = query.from(Message.class);
        query.select(builder.countDistinct(root.get("id")));
        addPredicates(query, builder,root, filters);
        return entityManager.createQuery(query).getSingleResult();
    }


    /**
     * Finds all ids of messages given a filter, order and page.
     * @param filters The filters to apply.
     * @param order The order to order by.
     * @param page The page requested.
     * @return List of all unique ids.
     */
    private List<Long> findAllIds(List<FilterCriteria> filters, OrderField order, PageRequest page) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);

        Root<Message> root = query.from(Message.class);
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
     * @param root Query message root.
     * @param order The order to be applied.
     */
    private <T> void addOrder(CriteriaQuery<T> query, CriteriaBuilder builder, Root<Message> root, OrderField order) {
        switch (order) {
            case DEFAULT:
            case DATE_DESCENDING: query.orderBy(builder.desc(root.get("publishDate")), builder.desc(root.get("id"))); break;
            case DATE_ASCENDING: query.orderBy(builder.asc(root.get("publishDate")), builder.desc(root.get("id"))); break;
            default: /** Do nothing for the rest of them */ break;
        }
    }

    /**
     * Applies the given filters to the query.
     * @param <T> The type of Criteria Query
     * @param query The query to be applied the filters.
     * @param builder Criteria query builder.
     * @param root Query message root.
     * @param filters List of criteria filters.
     */
    private <T> void addPredicates(CriteriaQuery<T> query, CriteriaBuilder builder, Root<Message> root, List<FilterCriteria> filters) {
        Predicate predicate = builder.conjunction();
        MessageCriteriaConsumer filterConsumer = new MessageCriteriaConsumer(predicate, builder, root);
        filters.stream().forEach(filterConsumer);
        predicate = filterConsumer.getPredicate();
        query.where(predicate);
    }
}
