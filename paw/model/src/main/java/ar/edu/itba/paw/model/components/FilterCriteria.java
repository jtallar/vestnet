package ar.edu.itba.paw.model.components;

import java.util.Objects;

/**
 * Filter Criteria for filtering repository calls
 * based on the field, operation, and value.
 */
public class FilterCriteria {
    private FilterCriteriaFields field;
    private Object value;

    public FilterCriteria(FilterCriteriaFields field, Object value) {
        this.field = field;
        this.value = value;
    }

    public FilterCriteriaFields getField() {
        return field;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FilterCriteria)) return false;
        FilterCriteria that = (FilterCriteria) o;
        return field.equals(that.field);
    }

    @Override
    public int hashCode() {
        return field.hashCode();
    }

    @Override
    public String toString() {
        return "FilterCriteria{" +
                "field='" + field + '\'' +
                ", value=" + value +
                '}';
    }
}
