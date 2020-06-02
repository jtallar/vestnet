package ar.edu.itba.paw.model.components;

/**
 * Enum for search fields
 * Must match the string on jsp and message from .
 */
public enum SearchField {
    PROJECT_NAME("feed.search.name"),
    PROJECT_SUMMARY("feed.search.summary"),
    OWNER_NAME("feed.search.owner.name"),
    OWNER_MAIL("feed.search.owner.mail"),
    PROJECT_LOCATION("feed.search.location");

    private String value;

    SearchField(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static SearchField getEnum(String value) {
        for (SearchField field : values())
            if (field.getValue().equals(value)) return field;
        return PROJECT_NAME;
    }

    @Override
    public String toString() {
        return "SearchField{" +
                "value='" + value + '\'' +
                '}';
    }
}
