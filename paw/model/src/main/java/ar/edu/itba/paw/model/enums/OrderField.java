package ar.edu.itba.paw.model.enums;

/**
 * Enum for sort options both for project and messages.
 */
public enum OrderField {

    PROJECT_DEFAULT(1, "hits"),
    PROJECT_FUNDING_TARGET_ASCENDING(2, "fundingTarget"),
    PROJECT_FUNDING_TARGET_DESCENDING(3, "fundingTarget"),
    PROJECT_ALPHABETICAL(4, "name"),

    /** This can be used for project or messages */
    DATE_ASCENDING(5, "publishDate"),
    DATE_DESCENDING(6, "publishDate");

    private int value;
    private String fieldName;

    OrderField(int value, String fieldName) {
        this.value = value;
        this.fieldName = fieldName;
    }

    /** Getters */

    public int getValue() {
        return value;
    }

    public String getField() {
        return fieldName;
    }


    /**
     * Gets the OrderField given the id.
      * @param id The id to search a match for.
     * @return The order field. PROJECT_DEFAULT when there is no match.
     */
    public static OrderField getEnum(int id) {
        for (OrderField value : values())
            if (value.getValue() == id) return value;
        return PROJECT_DEFAULT;
    }

    @Override
    public String toString() {
        return "ProjectSort{" +
                "value=" + value +
                "field='" + fieldName + '\'' +
                '}';
    }
}
