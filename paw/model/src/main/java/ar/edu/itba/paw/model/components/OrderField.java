package ar.edu.itba.paw.model.components;

/**
 * Enum for sort options
 * Must match the string for spring message.
 */
public enum OrderField {
    DEFAULT("feed.order.recommended"),
    DATE_ASCENDING("feed.order.date.asc"),
    DATE_DESCENDING("feed.order.date.desc"),
    COST_ASCENDING("feed.order.cost.asc"),
    COST_DESCENDING("feed.order.cost.desc"),
    ALPHABETICAL("feed.order.alpha");

    private String value;

    OrderField(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static OrderField getEnum(String method) {
        for (OrderField value : values())
            if (value.getValue().equals(method)) return value;
        return DEFAULT;
    }

    @Override
    public String toString() {
        return "ProjectSort{" +
                "value='" + value + '\'' +
                '}';
    }
}
