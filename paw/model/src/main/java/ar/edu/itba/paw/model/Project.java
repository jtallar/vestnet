package ar.edu.itba.paw.model;

import java.time.LocalDate;
import java.util.List;

/**
 * Models a project with all its properties.
 */
public class Project {
    private final long id;
    private final String name;

    private final String summary;
    private final LocalDate publishDate;
    private final LocalDate updateDate;
    private final long cost;
    private final long hits;

    // Si quiero poder inicializarlo despues, poner tamb final ownerId y sacarle el final a owner
    private final long ownerUserId;
    private User owner;
    private List<Category> categories;
    private Integer notRead; // TODO: SACAR DE ACA, pasarlo a metodo que recibe lista de ids como en favs

    public Integer getNotRead() {
        return notRead;
    }

    public void setNotRead(Integer notRead) {
        this.notRead = notRead;
    }

    public Project(long id, String name, String summary, LocalDate publishDate, LocalDate updateDate, long cost, long hits,
                   User owner, List<Category> categories) {
        this.id = id;
        this.name = name;
        this.summary = summary;
        this.publishDate = publishDate;
        this.updateDate = updateDate;
        this.cost = cost;
        this.hits = hits;
        this.owner = owner;
        this.ownerUserId = owner.getId();
        this.categories = categories;
    }

        public Project(long id, String name, String summary, LocalDate publishDate, LocalDate updateDate, long cost, long hits,
                   User owner, List<Category> categories, Integer notRead) {
        this.id = id;
        this.name = name;
        this.summary = summary;
        this.publishDate = publishDate;
        this.updateDate = updateDate;
        this.cost = cost;
        this.hits = hits;
        this.owner = owner;
        this.ownerUserId = owner.getId();
        this.categories = categories;
        this.notRead = notRead;
    }

    public Project(long id, String name, String summary, LocalDate publishDate, LocalDate updateDate, long cost, long hits,
                   long ownerUserId, List<Category> categories) {
        this.id = id;
        this.name = name;
        this.summary = summary;
        this.publishDate = publishDate;
        this.updateDate = updateDate;
        this.cost = cost;
        this.hits = hits;
        this.ownerUserId = ownerUserId;
        this.categories = categories;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSummary() {
        return summary;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public LocalDate getUpdateDate() {
        return updateDate;
    }

    public long getCost() {
        return cost;
    }

    public long getHits() {
        return hits;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public long getOwnerUserId() {
        return ownerUserId;
    }


    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public boolean hasCategory(String cat){
        boolean[] hasIt = {false};
        this.getCategories().forEach(category -> {
            if(category.getName().equals(cat)) hasIt[0] = true;
        });
        return hasIt[0];
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", summary='" + summary + '\'' +
                ", publishDate=" + publishDate +
                ", updateDate=" + updateDate +
                ", cost=" + cost +
                ", hits=" + hits +
                ", ownerUserId=" + ownerUserId +
                ", owner=" + owner +
                ", categories=" + categories +
                '}';
    }
}
