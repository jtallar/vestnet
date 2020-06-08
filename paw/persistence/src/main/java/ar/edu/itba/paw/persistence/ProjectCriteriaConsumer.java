package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.components.FilterCriteria;
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
            case "minCost": minCost(param.getValue()); break;
            case "maxCost": maxCost(param.getValue()); break;
            case "category": category(param.getValue()); break;
            case "owner": owner(param.getValue()); break;
            case "id": id(param.getValue()); break;
            case "ids": ids(param.getValue()); break;
            /** If its not a filter, its a search */
            default: search(param);
        }
    }


    /** Getters */

    public Predicate getPredicate() {
        return predicate;
    }


    /** Auxiliary functions */


    /**
     * For search criteria, depending on field, executes action.
     * @param param The parameters, field and value.
     */
    private void search(FilterCriteria param) {
        String keyword = param.getValue().toString().toLowerCase();
        switch (SearchField.getEnum(param.getField())) {
            case PROJECT_NAME: projectSearch("name", keyword); break;
            case PROJECT_SUMMARY: projectSearch("summary", keyword); break;
            case PROJECT_LOCATION: locationSearch(keyword); break;
            case OWNER_NAME: userNameSearch(keyword); break;
            case OWNER_MAIL: userSearch("email", keyword); break;
        }
    }


    /**
     * Filters by min cost.
     * @param value The min cost.
     */
    private void minCost(Object value) {
        predicate = builder.and(predicate, builder.greaterThanOrEqualTo(root.get("cost"), value.toString()));
    }


    /**
     * Filters by max cost.
     * @param value The max cost.
     */
    private void maxCost(Object value) {
        predicate = builder.and(predicate, builder.lessThanOrEqualTo(root.get("cost"), value.toString()));
    }


    /**
     * Filters by a single category.
     * @param value The category id.
     */
    private void category(Object value) {
        Join<Project, Category> categoryJoin = root.join("categories");
        predicate = builder.and(predicate, builder.equal(categoryJoin.get("id"), value.toString()));
    }


    /**
     * Filters by owner.
     * @param value The owner id.
     */
    private void owner(Object value) {
        predicate = builder.and(predicate, builder.equal(root.get("owner"), value));
    }


    /**
     * Filters by min cost.
     * @param value The min cost.
     */
    private void id(Object value) {
        predicate = builder.equal(root.get("id"), value.toString());
    }


    /**
     * Filters by a list of ids.
     * @param value The list of ids.
     */
    private void ids(Object value) {
        predicate = root.get("id").in((List) value);
    }


    /**
     * Filters by a string in a project column.
     * @param column The column to search for matches.
     * @param value The keyword to search.
     */
    private void projectSearch(String column, String value) {
        predicate = builder.and(predicate, builder.like(builder.lower(root.get(column)), "%" + value + "%"));
    }


    /**
     * Filters by a string in the user owner column.
     * @param column The column to search for matches.
     * @param value The keyword to search.
     */
    private void userSearch(String column, String value) {
        Join<Project, User> userJoin = root.join("owner");
        predicate = builder.and(predicate, builder.like(builder.lower(userJoin.get(column)), "%" + value + "%"));
    }


    /**
     * Filters by the full name of the user owner.
     * @param value The keyword.
     */
    private void userNameSearch(String value) {
        Join<Project, User> userJoin = root.join("owner");
        predicate = builder.and(predicate,
                builder.or(builder.like(builder.lower(userJoin.get("firstName")), "%" + value + "%"),
                builder.like(builder.lower(userJoin.get("lastName")), "%" + value + "%")));
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
        Join<User, City> cityJoin = locationJoin.join("state");

        predicate = builder.and(predicate,
                builder.or(builder.like(builder.lower(countryJoin.get("name")), "%" + value + "%"),
                        builder.like(builder.lower(stateJoin.get("name")), "%" + value + "%"),
                        builder.like(builder.lower(cityJoin.get("name")), "%" + value + "%")));
    }

}
