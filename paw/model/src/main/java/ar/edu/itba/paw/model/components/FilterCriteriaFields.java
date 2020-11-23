package ar.edu.itba.paw.model.components;

public enum FilterCriteriaFields {
    MIN_COST("minCost"),
    MAX_COST("maxCost"),
    OWNER("owner"),
    FUNDED("funded"),

    CATEGORY("id"),
    PROJECT_NAME("name"),
    PROJECT_SUMMARY("summary"),
    PROJECT_LOCATION(null),
    OWNER_NAME(null),
    OWNER_MAIL(null),

    IDS("id"),

    RECEIVER("receiver"),
    SENDER("sender"),
    PROJECT("project"),
    UNREAD("accepted"),
    ACCEPTED("accepted");

    private String fieldName;

    FilterCriteriaFields(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}