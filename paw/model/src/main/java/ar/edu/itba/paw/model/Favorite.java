package ar.edu.itba.paw.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "favorites")
public class Favorite implements Serializable {

    @Id
    @Column(name = "user_id", insertable = false, updatable = false)
    private int ownerId;

    @Id
    @Column(name = "project_id", insertable = false, updatable = false)
    private int projectId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Project project;

    /** Package */ Favorite() {

    }

    @Override
    public String toString() {
        return "Favorite{" +
                "ownerId=" + ownerId +
                ", projectId=" + projectId +
                '}';
    }
}
