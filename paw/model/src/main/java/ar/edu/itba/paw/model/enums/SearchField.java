package ar.edu.itba.paw.model.enums;

/**
 * Enum for search fields
 */
public enum SearchField {
    PROJECT_NAME(1),
    PROJECT_SUMMARY(2),
    OWNER_NAME(3),
    OWNER_MAIL(4),
    PROJECT_LOCATION(5);

    private int value;

    SearchField(int value) {
        this.value = value;
    }

    /** Getters */
    public int getValue() {
        return value;
    }


    /**
     * Gets the SearchField given the id.
     * @param id The id to search a match for.
     * @return The search field. PROJECT_NAME when there is no match.
     */
    public static SearchField getEnum(int id) {
        for (SearchField field : values())
            if (field.getValue() == id) return field;
        return PROJECT_NAME;
    }

    @Override
    public String toString() {
        return "SearchField{" +
                "value='" + value + '\'' +
                '}';
    }
}
