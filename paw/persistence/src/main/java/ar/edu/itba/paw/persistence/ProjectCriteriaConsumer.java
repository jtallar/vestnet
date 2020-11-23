package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.components.FilterCriteria;
import ar.edu.itba.paw.model.components.FilterCriteriaFields;
import ar.edu.itba.paw.model.components.SearchField;
import ar.edu.itba.paw.model.location.City;
import ar.edu.itba.paw.model.location.Country;
import ar.edu.itba.paw.model.location.Location;
import ar.edu.itba.paw.model.location.State;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.function.Consumer;

/**
 * Builds dynamically the query criteria.
 */

/** Protected */ class ProjectCriteriaConsumer implements Consumer<FilterCriteria> {
    private Predicate predicate;
    private CriteriaBuilder builder;
    private Root<Project> root;

    public ProjectCriteriaConsumer(Predicate predicate, CriteriaBuilder builder, Root<Project> root) {
        this.predicate = predicate;
        this.builder = builder;
        this.root = root;
    }

    @Override
    public void accept(FilterCriteria param) {
        switch (param.getField()) {
            case IDS: filterInList(param); break;
            case MIN_COST: filterGreaterOrEqual(param); break;
            case MAX_COST: filterLessOrEqual(param); break;
            case OWNER:
            case FUNDED: filterEquals(param); break;
            case CATEGORY: category(param); break;
            /** If its not a filter, its a search */
            case PROJECT_NAME: projectSearch("name", keyword); break;
            case PROJECT_SUMMARY: projectSearch("summary", keyword); break;
            case PROJECT_LOCATION: locationSearch(keyword); break;
            case OWNER_NAME: userNameSearch(keyword); break;
            case OWNER_MAIL: userSearch("email", keyword); break;
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
     * Filters by greater or equal to the field.
     * @param param The Filter Criteria param.
     */
    private void filterGreaterOrEqual(FilterCriteria param) {
        predicate = builder.and(predicate, builder.greaterThanOrEqualTo(root.get(param.getField().getFieldName()), param.getValue().toString()));
    }


    /**
     * Filters by less or equal to the field.
     * @param param The Filter Criteria param.
     */
    private void filterLessOrEqual(FilterCriteria param) {
        predicate = builder.and(predicate, builder.lessThanOrEqualTo(root.get(param.getField().getFieldName()), param.getValue().toString()));
    }

    /**
     * Filters by category.
     * @param param The Filter Criteria param.
     */
    private void filterCategory(FilterCriteria param) {
        Join<Project, Category> join = root.join("categories");
        predicate = builder.and(predicate, builder.equal(join.get(param.getField().getFieldName()), param.getValue().toString()));
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


    /**
     * Filters by a string in a project column.
     * @param column The column to search for matches.
     * @param value The keyword to search.
     */
    private void projectSearch(FilterCriteria param) {
        predicate = builder.and(predicate, builder.like(builder.lower(root.get(param.getField().getFieldName())), "%" + param.getValue()+ "%"));
    }


    /**
     * Filters by a string in the user owner column.
     * @param column The column to search for matches.
     * @param value The keyword to search.
     */
    private void userSearch(String column, String value) {
        Join<Project, User> join = root.join("owner");
        predicate = builder.and(predicate, builder.like(builder.lower(join.get(column)), "%" + value + "%"));
    }


    /**
     * Filters by the full name of the user owner.
     * @param value The keyword.
     */
    private void userNameSearch(String value) {
        Join<Project, User> join = root.join("owner");
        predicate = builder.and(predicate,
                builder.or(builder.like(builder.lower(join.get("firstName")), "%" + value + "%"),
                builder.like(builder.lower(join.get("lastName")), "%" + value + "%")));
    }


    /**
     * Filters by user owner location.
     * @param value The keyword.
     */
    private void locationSearch(String value) {
        Join<Project, User> userJoin = root.join("owner");
        Join<User, Location> locationJoin = userJoin.join("location");
        Join<User, Country> countryJoin = locationJoin.join("country");
        Join<User, State> stateJoin = locationJoin.join("state");
        Join<User, City> cityJoin = locationJoin.join("city");

        predicate = builder.and(predicate,
                builder.or(builder.like(builder.lower(countryJoin.get("name")), "%" + value + "%"),
                        builder.like(builder.lower(stateJoin.get("name")), "%" + value + "%"),
                        builder.like(builder.lower(cityJoin.get("name")), "%" + value + "%")));
    }

}
