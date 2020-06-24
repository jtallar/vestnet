package ar.edu.itba.paw.model;

import ar.edu.itba.paw.model.image.ProjectImage;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Models a project with all its properties.
 */
@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "projects_id_seq")
    @SequenceGenerator(sequenceName = "projects_id_seq", name = "projects_id_seq", allocationSize = 1)
    @Column(name = "id")
    private long id;

    @Column(name = "project_name", nullable = false)
    private String name;

    @Column(name = "summary", length = 250, nullable = false)
    private String summary;

    @Column(name = "cost", nullable = false)
    private long cost;

    @Column(name = "funded", nullable = false)
    private boolean funded;

    @Temporal(value = TemporalType.DATE)
    @Column(name = "publish_date", insertable = false)
    private Date publishDate;

    @Temporal(value = TemporalType.DATE)
    @Column(name = "update_date", insertable = false)
    private Date updateDate;

    @Column(name = "hits", nullable = false)
    private long hits;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User owner;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "project")
    private List<ProjectImage> images;

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

    @Column(name = "message_count" , nullable = false)
    private Integer msgCount;

    /** Protected */ Project() {
        /** For hibernate only */
    }



    public Project(String name, String summary, long cost, User owner, List<Category> categories) {
        this.name = name;
        this.summary = summary;
        this.cost = cost;
        this.owner = owner;
        this.categories = categories;
        this.hits = 0;
        this.funded = false;
        this.msgCount = 0;
    }


    /** Getters and setters */

    public Integer getMsgCount() {
        return msgCount;
    }

    public void setMsgCount(Integer msgCount) {
        this.msgCount = msgCount;
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

    public boolean isFunded() {
        return funded;
    }

    public void setFunded(boolean funded) {
        this.funded = funded;
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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<ProjectImage> getImages() {
        return images;
    }

    public void setImages(List<ProjectImage> images) {
        this.images = images;
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

    public List<User> getFavoriteBy() {
        return favoriteBy;
    }

    public void setFavoriteBy(List<User> favoriteBy) {
        this.favoriteBy = favoriteBy;
    }


    public void addMsgCount(){
        this.msgCount += 1;
    }

    public void decMsgCount(){
        this.msgCount -= 1;
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
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Project)) return false;
        Project project = (Project) o;
        return id == project.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}
