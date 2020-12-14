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
            case IDS: ids(param.getValue()); break;
            case MESSAGE_ENTREPRENEUR: owner(param.getValue()); break;
            case MESSAGE_INVESTOR: investor(param.getValue()); break;
            case MESSAGE_PROJECT: project(param.getValue()); break;
            case MESSAGE_SEEN: seen(); break;
            case MESSAGE_ACCEPTED: accepted(param.getValue()); break;
            case MESSAGE_DIRECTION: direction(param.getValue()); break;
            default: /** should not happen */ break;
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
    private void ids(Object value) {
        predicate = root.get("id").in((List) value);
    }


    /**
     * Filters by the project's owner.
     * @param value The user owner of of the project.
     */
    private void owner(Object value) {
        predicate = builder.and(predicate, builder.equal(root.get("owner"), value));
    }


    /**
     * Filters by the message sender.
     * @param value The user sender.
     */
    private void investor(Object value) {
        predicate = builder.and(predicate, builder.equal(root.get("investor"), value));
    }


    /**
     * Filters by the message project.
     * @param value The project.
     */
    private void project(Object value) {
        predicate = builder.and(predicate, builder.equal(root.get("project"), value));
    }


    /**
     * Filters unread messages.
     */
    private void seen() {
        predicate = builder.and(predicate, builder.isNull(root.get("seen")));
    }


    /**
     * Filters by the accepted status.
     * @param value The status to be checked.
     */
    private void accepted(Object value) {
        predicate = builder.and(predicate, builder.equal(root.get("accepted"), value));
    }


    /**
     * Filters by the direction status.
     * @param value The direction to be checked.
     */
    private void direction(Object value) {
        predicate = builder.and(predicate, builder.equal(root.get("direction"), value));
    }
}
