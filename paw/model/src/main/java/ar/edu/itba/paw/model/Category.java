package ar.edu.itba.paw.model;

import javax.persistence.*;
import java.util.List;

/**
 * Models a project category.
 */
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "category", length = 25, nullable = false)
    private  String name;

    @Column(name = "parent_id")
    private Long parent;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "categories")
    private List<Project> projectList;

    /** Protected */ Category() {
        /** For hibernate only */
    }

    public Category(long id, String name, long parent) {
        this.id = id;
        this.name = name;
        this.parent = parent;
    }

    public Category(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public List<Project> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<Project> projectList) {
        this.projectList = projectList;
    }

    @Override
    public String
    toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", parent=" + parent +
                '}';
    }
}
