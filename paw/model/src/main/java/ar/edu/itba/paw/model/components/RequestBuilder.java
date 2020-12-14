package ar.edu.itba.paw.model.components;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Abstract class to hold the gets of the criteria list and order
 * for a specific request.
 */
public abstract class RequestBuilder {
    protected Set<FilterCriteria> criteriaList;
    protected OrderField order;
    protected GroupField group;

    public RequestBuilder() {
        criteriaList = new HashSet<>();
        order = OrderField.DEFAULT;
        group = GroupField.NONE;
    }

    public List<FilterCriteria> getCriteriaList() {
        return new ArrayList<>(criteriaList);
    }

    public OrderField getOrder() {
        return order;
    }

    public GroupField getGroup() {
        return group;
    }
}
