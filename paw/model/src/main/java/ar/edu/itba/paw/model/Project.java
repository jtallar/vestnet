package ar.edu.itba.paw.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Models a project with all its properties.
 */
@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "projects_project_id_seq")
    @SequenceGenerator(sequenceName = "projects_project_id_seq", name = "projects_project_id_seq", allocationSize = 1)
    @Column(name = "id")
    private long id;

    @Column(name = "project_name", nullable = false)
    private String name;

    @Column(name = "summary", length = 250, nullable = false)
    private String summary;

    @Column(name = "cost", nullable = false)
    private long cost;

    @Temporal(value = TemporalType.DATE)
    @Column(name = "publish_date")
    private Date publishDate;

    @Temporal(value = TemporalType.DATE)
    @Column(name = "update_date")
    private Date updateDate;

    @Column(name = "hits", nullable = false)
    private long hits;

    @Column(name = "images")
    private byte[] image;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User owner;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "project_categories",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categories;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "project")
    private List<Message> messageList;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "favorites")
    private List<User> favoriteBy;

    /** Protected */ Project() {
        /** For hibernate only */
    }

    public Project(String name, String summary, long cost, byte[] image, User owner, List<Category> categories) {
        this.name = name;
        this.summary = summary;
        this.cost = cost;
        this.image = image;
        this.owner = owner;
        this.categories = categories;
    }

    public Project(long id) {
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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public long getHits() {
        return hits;
    }

    public void setHits(long hits) {
        this.hits = hits;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", summary='" + summary + '\'' +
                ", cost=" + cost +
                ", publishDate=" + publishDate +
                ", updateDate=" + updateDate +
                ", hits=" + hits +
                ", image=" + Arrays.toString(image) +
                ", owner=" + owner +
                ", categories=" + categories +
                '}';
    }
}
