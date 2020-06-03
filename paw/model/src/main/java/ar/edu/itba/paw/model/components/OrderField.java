package ar.edu.itba.paw.model.components;

/**
 * Enum for sort options
 * Must match the string for spring message.
 */
public enum OrderField {
    DEFAULT("1", "feed.order.recommended"),
    DATE_ASCENDING("2", "feed.order.date.asc"),
    DATE_DESCENDING("3", "feed.order.date.desc"),
    COST_ASCENDING("4", "feed.order.cost.asc"),
    COST_DESCENDING( "5","feed.order.cost.desc"),
    ALPHABETICAL( "6","feed.order.alpha");

    private String value;
    private String message;

    OrderField(String value, String message) {
        this.value = value;
        this.message = message;
    }

    public String getValue() {
        return value;
    }

    public String getMessage() {
        return message;
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
