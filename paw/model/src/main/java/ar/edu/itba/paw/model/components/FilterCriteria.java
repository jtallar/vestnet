package ar.edu.itba.paw.model.components;

public class FilterCriteria {
    String field;
    String operation;
    Object value;

    public FilterCriteria(String field, String operation, Object value) {
        this.field = field;
        this.operation = operation;
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public String getOperation() {
        return operation;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "FilterCriteria{" +
                "field='" + field + '\'' +
                ", operation='" + operation + '\'' +
                ", value=" + value +
                '}';
    }
}
