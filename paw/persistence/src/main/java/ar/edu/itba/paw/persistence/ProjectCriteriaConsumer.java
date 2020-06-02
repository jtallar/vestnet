package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.components.FilterCriteria;
import ar.edu.itba.paw.model.components.SearchField;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.function.Consumer;

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
            /** If its not a filter, its a search */
            default: search(param);
        }
    }

    private void search(FilterCriteria param) {
        switch (SearchField.getEnum(param.getField())) {
            case PROJECT_NAME: projectSearch("name", param.getValue()); break;
            case PROJECT_SUMMARY: projectSearch("summary", param.getValue()); break;
            case PROJECT_LOCATION: locationSearch(param.getValue()); break;
            case OWNER_NAME: userNameSearch(param.getValue()); break;
            case OWNER_MAIL: userSearch("email",param.getValue()); break;
        }
    }


    private void minCost(Object value) {
        predicate = builder.and(predicate, builder.greaterThanOrEqualTo(root.get("cost"), value.toString()));
    }

    private void maxCost(Object value) {
        predicate = builder.and(predicate, builder.lessThanOrEqualTo(root.get("cost"), value.toString()));
    }

    private void category(Object value) {
        Join<Project, Category> categoryJoin = root.join("categories");
        predicate = builder.and(predicate, builder.equal(categoryJoin.get("id"), value.toString()));
    }

    private void projectSearch(String column, Object value) {
        predicate = builder.and(predicate, builder.like(root.get(column), "%" + value.toString() + "%"));
    }

    private void userSearch(String column, Object value) {
        Join<Project, User> userJoin = root.join("owner");
        predicate = builder.and(predicate, builder.like(userJoin.get(column), "%" + value.toString() + "%"));
    }

    private void userNameSearch(Object value) {
        Join<Project, User> userJoin = root.join("owner");
        predicate = builder.and(predicate,
                builder.or(builder.like(userJoin.get("firstName"), "%" + value.toString() + "%"),
                builder.like(userJoin.get("lastName"), "%" + value.toString() + "%")));
    }

    private void locationSearch(Object value) {
        Join<Project, User> userJoin = root.join("owner");
        Join<User, Location> locationJoin = userJoin.join("location");
        Join<User, Country> countryJoin = locationJoin.join("country");
        Join<User, State> stateJoin = locationJoin.join("state");
        Join<User, City> cityJoin = locationJoin.join("state");

        predicate = builder.and(predicate,
                builder.or(builder.like(countryJoin.get("name"), "%" + value.toString() + "%"),
                        builder.like(stateJoin.get("name"), "%" + value.toString() + "%"),
                        builder.like(cityJoin.get("name"), "%" + value.toString() + "%")));
    }


    public Predicate getPredicate() {
        return predicate;
    }
}
