package ar.edu.itba.paw.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "favorites")
public class Favorite implements Serializable {

    @Id
    @Column(name = "user_id", insertable = false, updatable = false)
    private long userId;

    @Id
    @Column(name = "project_id", insertable = false, updatable = false)
    private long projectId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Project project;

    /** Package */ Favorite() {

    }

    public Favorite(User user, Project project) {
        this.user = user;
        this.project = project;
    }

    public long getUserId() {
        return userId;
    }

    public long getProjectId() {
        return projectId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    /** Getters and Setters */



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Favorite)) return false;
        Favorite favorite = (Favorite) o;
        return userId == favorite.userId && projectId == favorite.projectId;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(userId) ^ Long.hashCode(projectId);
    }

    @Override
    public String toString() {
        return "Favorite{" +
                "ownerId=" + userId +
                ", projectId=" + projectId +
                '}';
    }
}
