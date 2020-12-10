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
            case PROJECT_MIN_COST: minCost(param.getValue()); break;
            case PROJECT_MAX_COST: maxCost(param.getValue()); break;
            case PROJECT_CATEGORY: category(param.getValue()); break;
            case PROJECT_OWNER: owner(param.getValue()); break;
            case PROJECT_FUNDED: funded(param.getValue()); break;
            case IDS: ids(param.getValue()); break;

            /** If its not a filter, its a search */
            default:
                /** Escape all the postgres special characters */
                String searchVal = escapeCharacters(param.getValue().toString(), new String[]{"\\", "%", "_", "\"", "'"});
                switch (param.getField()) {
                    case PROJECT_SEARCH_NAME: projectSearch("name", searchVal); break;
                    case PROJECT_SEARCH_SUMMARY: projectSearch("summary", searchVal); break;
                    case PROJECT_SEARCH_LOCATION: locationSearch(searchVal); break;
                    case PROJECT_SEARCH_OWNER_NAME: userNameSearch(searchVal); break;
                    case PROJECT_SEARCH_OWNER_MAIL: userSearch(searchVal); break;
                    default: /** should not happen */ break;
                }
        }
    }

    /** Getters */

    public Predicate getPredicate() {
        return predicate;
    }


    /** Auxiliary functions */


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
     * Filter by project fully founded or not.
     * @param value Boolean funded.
     */
    private void funded(Object value) {
        predicate = builder.and(predicate, builder.equal(root.get("funded"), value));
    }


    /**
     * Filters by a list of ids.
     * @param value The list of ids.
     */
    private void ids(Object value) {
        predicate = root.get("id").in((List) value);
    }


    /**
     * Escapes all the characters given on the input string.
     * @param input The input string to escape all the special characters.
     * @param specialCharacters The special characters to escape.
     * @return The new string with special characters escaped.
     */
    private static String escapeCharacters(String input, final String[] specialCharacters) {
        for (String specialCharacter : specialCharacters) {
            if (input.contains(specialCharacter)) {
                input = input.replace(specialCharacter, "\\" + specialCharacter);
            }
        }
        return input;
    }


    /**
     * Filters by a string in a project column.
     * @param column The column to search for matches.
     * @param keyword The keyword to search.
     */
    private void projectSearch(String column, String keyword) {
        predicate = builder.and(predicate, builder.like(builder.lower(root.get(column)), "%" + keyword + "%"));
    }


    /**
     * Filters by a string in the user owner column.
     * @param keyword The keyword to search.
     */
    private void userSearch(String keyword) {
        Join<Project, User> userJoin = root.join("owner");
        predicate = builder.and(predicate, builder.like(builder.lower(userJoin.get("mail")), "%" + keyword + "%"));
    }


    /**
     * Filters by the full name of the user owner.
     * @param keyword The keyword.
     */
    private void userNameSearch(String keyword) {
        Join<Project, User> userJoin = root.join("owner");
        predicate = builder.and(predicate,
                builder.or(builder.like(builder.lower(userJoin.get("firstName")), "%" + keyword + "%"),
                builder.like(builder.lower(userJoin.get("lastName")), "%" + keyword + "%")));
    }


    /**
     * Filters by user owner location.
     * @param keyword The keyword.
     */
    private void locationSearch(String keyword) {
        Join<Project, User> userJoin = root.join("owner");
        Join<User, Location> locationJoin = userJoin.join("location");
        Join<User, Country> countryJoin = locationJoin.join("country");
        Join<User, State> stateJoin = locationJoin.join("state");
        Join<User, City> cityJoin = locationJoin.join("city");

        predicate = builder.and(predicate,
                builder.or(builder.like(builder.lower(countryJoin.get("name")), "%" + keyword + "%"),
                        builder.like(builder.lower(stateJoin.get("name")), "%" + keyword + "%"),
                        builder.like(builder.lower(cityJoin.get("name")), "%" + keyword + "%")));
    }

}
