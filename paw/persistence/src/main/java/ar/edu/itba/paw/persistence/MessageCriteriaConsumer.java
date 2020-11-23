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
            case IDS: filterInList(param); break;
            case UNREAD: filterIsNull(param); break;
            case RECEIVER:
            case SENDER:
            case PROJECT:
            case ACCEPTED: filterEquals(param); break;
        }
    }


    /** Getters */

    public Predicate getPredicate() {
        return predicate;
    }


    /** Auxiliary functions */


    /**
     * Filters if are in list. Generally for IDs.
     * @param param The Filter Criteria with the list and field.
     */
    private void filterInList(FilterCriteria param) {
        predicate = root.get(param.getField().getFieldName()).in((List) param.getValue());
    }


    /**
     * Filters by the field for those that are null.
     * @param param The Filter Criteria with the field to be used.
     */
    private void filterIsNull(FilterCriteria param) {
        predicate = builder.and(predicate, builder.isNull(root.get(param.getField().getFieldName())));
    }


    /**
     * Filters by Filter Criteria parameters.
     * Filters those to equal object passed in value.
     * The field to filter by is passed on the field.
     * @param param The Filter Criteria parameters.
     */
    private void filterEquals(FilterCriteria param) {
        predicate = builder.and(predicate, builder.equal(root.get(param.getField().getFieldName()), param.getValue()));
    }
}
