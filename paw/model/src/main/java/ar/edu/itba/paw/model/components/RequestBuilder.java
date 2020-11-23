package ar.edu.itba.paw.model.components;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class RequestBuilder {
    protected Set<FilterCriteria> criteriaList;
    protected OrderField order;

    public RequestBuilder() {
        criteriaList = new HashSet<>();
        order = OrderField.DEFAULT;
    }

    public List<FilterCriteria> getCriteriaList() {
        return new ArrayList<>(criteriaList);
    }

    public OrderField getOrder() {
        return order;
    }
}
