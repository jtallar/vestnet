package ar.edu.itba.paw.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "project_stages")
public class ProjectStages {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "project_stages_id_seq")
    @SequenceGenerator(sequenceName = "project_stages_id_seq", name = "project_stages_id_seq", allocationSize = 1)
    @Column(name = "id")
    private long id;

    @Column(name = "number")
    private long number;

    @Column(name = "name")
    private String name;

    @Column(name = "comment")
    private String comment;

    @Column(name = "completed")
    private boolean completed;

    @Temporal(TemporalType.DATE)
    @Column(name = "completed_date")
    private Date completedDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Project project;

    /** Protected */ ProjectStages() {
        /** For hibernate only */
    }

    public ProjectStages(String name, long number, long projectId) {
        this.name = name;
        this. number = number;
        this.project = new Project(projectId);
        this.completed = false;
    }

    /** Getters and Setters */

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted() {
        this.completed = true;
        this.completedDate = new Date();
    }

    public Date getCompletedDate() {
        return completedDate;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }


    /** Equals, hashCode and toString */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjectStages)) return false;
        ProjectStages that = (ProjectStages) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ProjectStages{" +
                "id=" + id +
                ", number=" + number +
                ", name='" + name + '\'' +
                ", comment='" + comment + '\'' +
                ", completed=" + completed +
                ", completedDate=" + completedDate +
                '}';
    }
}
