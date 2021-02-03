package ar.edu.itba.paw.model.image;

import ar.edu.itba.paw.model.Project;

import javax.persistence.*;

@Entity
@Table(name = "project_images")
public class ProjectImage extends Image {


    @Column(name = "main")
    private boolean main;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Project project;


    /** Protected */ ProjectImage() {
        /* For hibernate only */
    }

    public ProjectImage(Project project, byte[] image, boolean main) {
        super(image);
        this.project = project;
        this.main = main;
    }

    public ProjectImage(long id) {
        super(id);
    }

    /** Getters and setters */

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public boolean isMain() {
        return main;
    }

    public boolean isNotMain() {
        return !main;
    }

    public void setMain(boolean main) {
        this.main = main;
    }
}