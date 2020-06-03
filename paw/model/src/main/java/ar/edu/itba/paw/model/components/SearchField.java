package ar.edu.itba.paw.model.components;

/**
 * Enum for search fields
 * Must match the string on jsp and message from .
 */
public enum SearchField {
    PROJECT_NAME("1", "feed.search.name"),
    PROJECT_SUMMARY("2", "feed.search.summary"),
    OWNER_NAME("3", "feed.search.owner.name"),
    OWNER_MAIL("4", "feed.search.owner.mail"),
    PROJECT_LOCATION("5", "feed.search.location");

    private String value;
    private String message;

    SearchField(String value, String message) {
        this.value = value;
        this.message = message;
    }

    public String getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public static SearchField getEnum(String id) {
        for (SearchField field : values())
            if (field.getValue().equals(id)) return field;
        return PROJECT_NAME;
    }

    @Override
    public String toString() {
        return "SearchField{" +
                "value='" + value + '\'' +
                '}';
    }
}
