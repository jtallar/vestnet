package ar.edu.itba.paw.model.components;

import ar.edu.itba.paw.model.User;

import static ar.edu.itba.paw.model.components.FilterField.*;

/**
 * Creates the list of criteria filter and order for
 * the project requests.
 */
public class ProjectRequestBuilder extends RequestBuilder {

    public ProjectRequestBuilder() {
        super();
    }


    /**
     * Sets to filter a category.
     * @param category Category id to filter. None if null.
     * @return The RequestBuilder.
     */
    public ProjectRequestBuilder setCategory(Integer category) {
        if (category != null)
            criteriaList.add(new FilterCriteria(PROJECT_CATEGORY, category));
        return this;
    }


    /**
     * Sets to filter the min value of cost.
     * @param min Min value to filter. None if null.
     * @return The RequestBuilder.
     */
    public ProjectRequestBuilder setCostMin(Integer min) {
        if (min != null)
            criteriaList.add(new FilterCriteria(PROJECT_MIN_COST, min));
        return this;
    }


    /**
     * Sets to filter the max value of cost.
     * @param max Max value to filter. None if null.
     * @return The RequestBuilder.
     */
    public ProjectRequestBuilder setCostMax(Integer max) {
        if (max != null)
            criteriaList.add(new FilterCriteria(PROJECT_MAX_COST, max));
        return this;
    }


    /**
     * Sets to filter the min value of cost.
     * @param min Min value to filter. None if null.
     * @param max Max value to filter. None if null.
     * @return The RequestBuilder.
     */
    public ProjectRequestBuilder setCostRange(Integer min, Integer max) {
        setCostMin(min);
        return setCostMax(max);
    }


    /**
     * Sets to filter by funded or not.
     * @param state State of funded to filter by.
     * @return The RequestBuilder.
     */
    public ProjectRequestBuilder setFunded(boolean state) {
        criteriaList.add(new FilterCriteria(PROJECT_FUNDED, state));
        return this;
    }


    /**
     * Sets to filter and search by keyword in a specific field.
     * @param keyword The keyword to search. None if null or empty.
     * @param field The field to search.
     * @return The Request Builder.
     */
    public ProjectRequestBuilder setSearch(String keyword, int field) {
        if (keyword == null || keyword.isEmpty()) return this;
        FilterField finalField = FilterField.fromSearchField(SearchField.getEnum(String.valueOf(field)));
        criteriaList.add(new FilterCriteria(finalField, keyword.toLowerCase()));
        return this;
    }


    /**
     * Sets to filter the owner.
     * @param id The owner id to filter.
     * @return The RequestBuilder.
     */
    public ProjectRequestBuilder setOwner(long id) {
        criteriaList.add(new FilterCriteria(PROJECT_OWNER, new User(id)));
        return this;
    }


    /**
     * Sets to order the request.
     * @param order Value of the order, if not valid, default order is set.
     * @return The RequestBuilder.
     */
    public ProjectRequestBuilder setOrder(int order) {
        this.order = OrderField.getEnum(String.valueOf(order));
        return this;
    }


    /**
     * Sets to order the request.
     * @param order OrderField to set.
     * @return The RequestBuilder.
     */
    public ProjectRequestBuilder setOrder(OrderField order) {
        this.order = order;
        return this;
    }
}
