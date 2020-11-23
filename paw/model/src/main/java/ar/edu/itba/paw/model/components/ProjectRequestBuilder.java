package ar.edu.itba.paw.model.components;

import ar.edu.itba.paw.model.User;

public class ProjectRequestBuilder extends RequestBuilder {

    public ProjectRequestBuilder() {
        super();
    }

    public ProjectRequestBuilder setCategory(Integer category) {
        if (category != null)
            criteriaList.add(new FilterCriteria("category", category));
        return this;
    }

    public ProjectRequestBuilder setCostMin(Integer min) {
        if (min != null)
            criteriaList.add(new FilterCriteria("minCost", min));
        return this;
    }

    public ProjectRequestBuilder setCostMax(Integer max) {
        if (max != null)
            criteriaList.add(new FilterCriteria("maxCost", max));
        return this;
    }

    public ProjectRequestBuilder setCostRange(Integer min, Integer max) {
        setCostMin(min);
        return setCostMax(max);
    }

    public ProjectRequestBuilder setFunded(boolean state) {
        criteriaList.add(new FilterCriteria("funded", state));
        return this;
    }

    public ProjectRequestBuilder setSearch(String keyword, int field) {
        if (keyword != null && !keyword.isEmpty())
            criteriaList.add(new FilterCriteria(String.valueOf(field), keyword));
        return this;
    }

    public ProjectRequestBuilder setOwner(long id) {
        criteriaList.add(new FilterCriteria("owner", new User(id)));
        return this;
    }

    public ProjectRequestBuilder setOrder(int order) {
        this.order = OrderField.getEnum(String.valueOf(order));
        return this;
    }

    public ProjectRequestBuilder setOrder(OrderField order) {
        this.order = order;
        return this;
    }
}
