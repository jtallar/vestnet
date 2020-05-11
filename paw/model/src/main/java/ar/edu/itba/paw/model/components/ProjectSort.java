package ar.edu.itba.paw.model.components;

import ar.edu.itba.paw.model.User;

public enum ProjectSort {
    DEFAULT("default", 0),
    DATE("date", 1),
    COST_ASCENDING("cost_ascending", 2),
    COST_DESCENDING("cost_descending", 3),
    ALPHABETICAL("alphabetical", 4);

    private String method;
    private int id;

    ProjectSort(String method, Integer id) {
        this.method = method;
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public int getId() {
        return id;
    }

    public static ProjectSort getEnum(String method) {
        for (ProjectSort value : values())
            if (value.getMethod().equals(method)) return value;
        return DEFAULT;
    }
}
