package ar.edu.itba.paw.model;

public class Category {
    private final long id;
    private final String name;
    // TODO: QUE PASA SI NO TIENE PADRE? VA EN NULL O EN 0? SI ESTA EN 0, NO HAY CATEGORIA 0?
    private final long parentId;

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
