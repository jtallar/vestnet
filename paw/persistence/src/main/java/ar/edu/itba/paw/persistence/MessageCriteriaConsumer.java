package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Message;
import ar.edu.itba.paw.model.components.FilterCriteria;
import ar.edu.itba.paw.model.enums.FilterField;

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
        FilterField filter = param.getField();
        switch (filter) {
            case IDS: in(filter.getField(), param.getValue()); break;
            case MESSAGE_ANSWERED: notNull(filter.getField()); break;
            case MESSAGE_ENTREPRENEUR:
            case MESSAGE_INVESTOR:
            case MESSAGE_PROJECT:
            case MESSAGE_SEEN:
            case MESSAGE_SEEN_ANSWER:
            case MESSAGE_ACCEPTED:
            case MESSAGE_DIRECTION: equal(filter.getField(), param.getValue()); break;
            default: /** should not happen */ break;
        }
    }


    /** Getters */

    public Predicate getPredicate() {
        return predicate;
    }


    /** Auxiliary functions */

    /**
     * Filters by IN a list.
     * @param field The field to filter by
     * @param value The list to filter.
     */
    private void in(String field, Object value) {
        predicate = root.get(field).in((List) value);
    }


    /**
     * Filters by not null a field.
     * @param field The field to filter by
     */
    private void notNull(String field) {
        predicate = builder.and(predicate, builder.isNotNull(root.get(field)));
    }


    /**
     * Filters by equal of the field.
     * @param field The field to
     * @param value The value to search equals.
     */
    private void equal(String field, Object value) {
        predicate = builder.and(predicate, builder.equal(root.get(field), value));
    }
}
