package ar.edu.itba.paw.model;

public class Category {
    private final long id;
    private final String name;
    private final Category parent;

    public Category(long id, String name, Category parent) {
        this.id = id;
        this.name = name;
        this.parent = parent;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Category getParent() {
        return parent;
    }
}
