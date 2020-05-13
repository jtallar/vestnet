package ar.edu.itba.paw.model;

/**
 * Models a project category.
 */
public class Category {

    private final long id;
    private final String name;
    private final long parentId; // If root category, parentId = 0.

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

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", parentId=" + parentId +
                '}';
    }
}
