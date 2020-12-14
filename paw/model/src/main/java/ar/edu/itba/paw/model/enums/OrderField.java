package ar.edu.itba.paw.model.enums;

/**
 * Enum for sort options
 * Must match the string for spring message.
 */
public enum OrderField {
    DEFAULT("1", "order.recommended", "hits"),
    DATE_ASCENDING("2", "order.date.asc", "publishDate"),
    DATE_DESCENDING("3", "order.date.desc", "publishDate"),
    COST_ASCENDING("4", "order.cost.asc", "cost"),
    COST_DESCENDING( "5","order.cost.desc", "cost"),
    ALPHABETICAL( "6","order.alpha", "name");

    private String value;
    private String message;
    private String fieldName;

    OrderField(String value, String message, String fieldName) {
        this.value = value;
        this.message = message;
        this.fieldName = fieldName;
    }

    public String getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public String getField() {
        return fieldName;
    }

    public static OrderField getEnum(String id) {
        for (OrderField value : values())
            if (value.getValue().equals(id)) return value;
        return DEFAULT;
    }

    @Override
    public String toString() {
        return "ProjectSort{" +
                "value='" + value + '\'' +
                '}';
    }
}
