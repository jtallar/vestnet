package ar.edu.itba.paw.model;

public class Category {
    private final long id;
    private final String name;
    private final long parentId; // Si no tiene padre, parentId = 0

    public Category(long id, String name, long parentId) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
    }


    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getParent() {
        return parentId;
    }
}
