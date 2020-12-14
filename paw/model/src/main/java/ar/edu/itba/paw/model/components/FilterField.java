package ar.edu.itba.paw.model.components;

public enum FilterField {
    PROJECT_MIN_COST,
    PROJECT_MAX_COST,
    PROJECT_CATEGORY,
    PROJECT_OWNER,
    PROJECT_FUNDED,
    IDS,
    PROJECT_SEARCH_NAME,
    PROJECT_SEARCH_SUMMARY,
    PROJECT_SEARCH_LOCATION,
    PROJECT_SEARCH_OWNER_NAME,
    PROJECT_SEARCH_OWNER_MAIL,
    MESSAGE_ENTREPRENEUR,
    MESSAGE_INVESTOR,
    MESSAGE_PROJECT,
    MESSAGE_SEEN,
    MESSAGE_ACCEPTED;

    public static FilterField fromSearchField(SearchField searchField) {
        switch (searchField) {
            case OWNER_MAIL: return PROJECT_SEARCH_OWNER_MAIL;
            case OWNER_NAME: return PROJECT_SEARCH_OWNER_NAME;
            case PROJECT_SUMMARY: return PROJECT_SEARCH_SUMMARY;
            case PROJECT_LOCATION: return PROJECT_SEARCH_LOCATION;
            default: return PROJECT_SEARCH_NAME;
        }
    }
}
