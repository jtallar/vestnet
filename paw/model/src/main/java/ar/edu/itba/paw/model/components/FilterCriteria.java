package ar.edu.itba.paw.model.components;

/**
 * Filter Criteria for filtering repository calls
 * based on the field, operation, and value.
 */
public class FilterCriteria {
    String field;
    Object value;

    public FilterCriteria(String field, Object value) {
        this.field = field;
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "FilterCriteria{" +
                "field='" + field + '\'' +
                ", value=" + value +
                '}';
    }
}
