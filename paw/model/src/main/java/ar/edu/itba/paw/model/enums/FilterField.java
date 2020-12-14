package ar.edu.itba.paw.model.enums;

/**
 * Filter fields used in filter criteria for JPA Criteria Builder
 */
public enum FilterField {
    PROJECT_MIN_COST,
    PROJECT_MAX_COST,
    PROJECT_CATEGORY,
    PROJECT_OWNER,
    PROJECT_FUNDED,
    PROJECT_SEARCH_NAME,
    PROJECT_SEARCH_SUMMARY,
    PROJECT_SEARCH_LOCATION,
    PROJECT_SEARCH_OWNER_NAME,
    PROJECT_SEARCH_OWNER_MAIL,

    /** Valid field names */
    IDS("id"),
    MESSAGE_ENTREPRENEUR("owner"),
    MESSAGE_INVESTOR("investor"),
    MESSAGE_PROJECT("project"),
    MESSAGE_SEEN("seen"),
    MESSAGE_ACCEPTED("accepted");

    private String fieldName;

    FilterField(String fieldName) {
        this.fieldName = fieldName;
    }

    FilterField() {
        this.fieldName = "";
    }

    /** Getters */

    public String getField() {
        return fieldName;
    }

    /**
     * Converts the SearchField enum to Filter Field
     * @param searchField The search field to be converted.
     * @return The new filter field. The default is PROJECT_SEARCH_NAME.
     */
    public static FilterField fromSearchField(SearchField searchField) {
        switch (searchField) {
            case OWNER_MAIL: return PROJECT_SEARCH_OWNER_MAIL;
            case OWNER_NAME: return PROJECT_SEARCH_OWNER_NAME;
            case PROJECT_SUMMARY: return PROJECT_SEARCH_SUMMARY;
            case PROJECT_LOCATION: return PROJECT_SEARCH_LOCATION;
            default: return PROJECT_SEARCH_NAME;
        }
    }

    @Override
    public String toString() {
        return "FilterField{" +
                "fieldName='" + fieldName + '\'' +
                '}';
    }
}
