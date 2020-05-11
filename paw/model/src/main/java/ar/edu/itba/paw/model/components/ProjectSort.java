package ar.edu.itba.paw.model.components;

import ar.edu.itba.paw.model.User;

public enum ProjectSort {
    DEFAULT("default", 0),
    DATE("date", 1),
    COST_A("cost_asc", 2),
    COST_D("cost_desc", 3),
    ALPHA("alpha", 4);

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
}
