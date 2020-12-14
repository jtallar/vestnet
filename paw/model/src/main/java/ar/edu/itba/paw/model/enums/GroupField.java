package ar.edu.itba.paw.model.enums;

/**
 * Group Field specifically for messages.
 * If they are grouped, then
 */
public enum GroupField {
    NONE("id"),
    PROJECT("project"),
    INVESTOR("investor");

    private String fieldName;


    GroupField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getField() {
        return fieldName;
    }
}
