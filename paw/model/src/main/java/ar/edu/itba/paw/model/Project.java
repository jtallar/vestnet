package ar.edu.itba.paw.model;

import ar.edu.itba.paw.model.image.ProjectImage;
import ar.edu.itba.paw.model.location.Location;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Models a project with all its properties.
 */
@Entity
@Table(name = "projects")
public class Project {
    public static final int MAX_STAGES = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "projects_id_seq")
    @SequenceGenerator(sequenceName = "projects_id_seq", name = "projects_id_seq", allocationSize = 1)
    @Column(name = "id")
    private long id;

    @Column(name = "project_name", nullable = false)
    private String name;

    @Column(name = "summary", length = 250, nullable = false)
    private String summary;

    @Column(name = "funding_target", nullable = false)
    private long fundingTarget;

    @Column(name = "funding_current", nullable = false)
    private long fundingCurrent;

    @Column(name = "closed", nullable = false)
    private boolean closed;

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

    @Column(name = "owner_id", insertable = false, updatable = false)
    private long ownerId;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "project")
    private Set<ProjectImage> images;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "project_categories",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categories;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "favoriteProjects")
    private List<User> favoriteBy;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "project")
    private Set<Favorite> favorites;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "project")
    private Set<ProjectStages> stages;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false, orphanRemoval = true)
    @JoinColumn(name = "stats_id")
    private ProjectStats stats;


    /** Protected */ Project() {
        /** For hibernate only */
    }


    public Project(String name, String summary, long target, User owner) {
        this.name = name;
        this.summary = summary;
        this.fundingTarget = target;
        this.owner = owner;
        this.hits = 0;
        this.closed = false;
        this.stats = new ProjectStats(true);
    }


    /** Getters and setters */

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

    public long getFundingTarget() {
        return fundingTarget;
    }

    public void setFundingTarget(long fundingTarget) {
        this.fundingTarget = fundingTarget;
    }

    public long getFundingCurrent() {
        return fundingCurrent;
    }

    public void setFundingCurrent(long fundingCurrent) {
        this.fundingCurrent = fundingCurrent;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
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

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public Set<ProjectImage> getImages() {
        return images;
    }

    public void setImages(Set<ProjectImage> images) {
        this.images = images;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<User> getFavoriteBy() {
        return favoriteBy;
    }

    public void setFavoriteBy(List<User> favoriteBy) {
        this.favoriteBy = favoriteBy;
    }

    public Set<Favorite> getFavorites() {
        return favorites;
    }

    public void setFavorites(Set<Favorite> favorites) {
        this.favorites = favorites;
    }

    public Set<ProjectStages> getStages() {
        return stages;
    }

    public void setStages(Set<ProjectStages> stages) {
        this.stages = stages;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", summary='" + summary + '\'' +
                ", target=" + fundingTarget +
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
