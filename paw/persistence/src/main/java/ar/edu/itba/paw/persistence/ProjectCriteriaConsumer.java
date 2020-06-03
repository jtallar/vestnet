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
        String value = param.getValue().toString();
        switch (param.getField()) {
            case "minCost": minCost(value); break;
            case "maxCost": maxCost(value); break;
            case "category": category(value); break;
            /** If its not a filter, its a search */
            default: search(param);
        }
    }

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


    private void minCost(String value) {
        predicate = builder.and(predicate, builder.greaterThanOrEqualTo(root.get("cost"), value));
    }

    private void maxCost(String value) {
        predicate = builder.and(predicate, builder.lessThanOrEqualTo(root.get("cost"), value));
    }

    private void category(String value) {
        Join<Project, Category> categoryJoin = root.join("categories");
        predicate = builder.and(predicate, builder.equal(categoryJoin.get("id"), value));
    }

    private void projectSearch(String column, String value) {
        predicate = builder.and(predicate, builder.like(builder.lower(root.get(column)), "%" + value + "%"));
    }

    private void userSearch(String column, String value) {
        Join<Project, User> userJoin = root.join("owner");
        predicate = builder.and(predicate, builder.like(builder.lower(userJoin.get(column)), "%" + value + "%"));
    }

    private void userNameSearch(String value) {
        Join<Project, User> userJoin = root.join("owner");
        predicate = builder.and(predicate,
                builder.or(builder.like(builder.lower(userJoin.get("firstName")), "%" + value + "%"),
                builder.like(builder.lower(userJoin.get("lastName")), "%" + value + "%")));
    }

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


    public Predicate getPredicate() {
        return predicate;
    }
}
