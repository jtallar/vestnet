package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Message;
import ar.edu.itba.paw.model.components.FilterCriteria;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.function.Consumer;

/**
 * Builds dynamically the query criteria.
 */

/** Protected */ class MessageCriteriaConsumer implements Consumer<FilterCriteria> {
    private Predicate predicate;
    private CriteriaBuilder builder;
    private Root<Message> root;

    public MessageCriteriaConsumer(Predicate predicate, CriteriaBuilder builder, Root<Message> root) {
        this.predicate = predicate;
        this.builder = builder;
        this.root = root;
    }

    @Override
    public void accept(FilterCriteria param) {
        switch (param.getField()) {
            case "ids": ids(param.getValue()); break;
            case "receiver": receiver(param.getValue()); break;
            case "sender": sender(param.getValue()); break;
            case "project": project(param.getValue()); break;
            case "unread": unread(); break;
            case "accepted": accepted(param.getValue()); break;
        }
    }


    /** Getters */

    public Predicate getPredicate() {
        return predicate;
    }


    /** Auxiliary functions */

    /**
     * Filters by ids.
     * @param value The list of ids.
     */
    public void ids(Object value) {
        predicate = root.get("id").in((List) value);
    }


    /**
     * Filters by the message receiver.
     * @param value The user receiver.
     */
    public void receiver(Object value) {
        predicate = builder.and(predicate, builder.equal(root.get("receiver"), value));
    }


    /**
     * Filters by the message sender.
     * @param value The user sender.
     */
    public void sender(Object value) {
        predicate = builder.and(predicate, builder.equal(root.get("sender"), value));
    }


    /**
     * Filters by the message project.
     * @param value The project.
     */
    public void project(Object value) {
        predicate = builder.and(predicate, builder.equal(root.get("project"), value));
    }


    /**
     * Filters unread messages.
     */
    public void unread() {
        predicate = builder.and(predicate, builder.isNull(root.get("accepted")));
    }


    /**
     * Filters by the accepted status.
     * @param value The status to be checked.
     */
    public void accepted(Object value) {
        predicate = builder.and(predicate, builder.equal(root.get("accepted"), value));
    }
}