package ar.edu.itba.paw.model.enums;

/**
 * Group Field specifically for messages.
 */
public enum GroupField {
    NONE("id"),
    PROJECT("project"),
    INVESTOR("investor");

    private String fieldName;

    GroupField(String fieldName) {
        this.fieldName = fieldName;
    }

    /** Getters */

    public String getField() {
        return fieldName;
    }

    @Override
    public String toString() {
        return "GroupField{" +
                "fieldName='" + fieldName + '\'' +
                '}';
    }
}
