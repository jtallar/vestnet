package ar.edu.itba.paw.model.image;

import ar.edu.itba.paw.model.Project;

import javax.persistence.*;

@Entity
@Table(name = "project_images")
public class ProjectImage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "project_images_id_seq")
    @SequenceGenerator(sequenceName = "project_images_id_seq", name = "project_images_id_seq", allocationSize = 1)
    @Column(name = "id")
    private long id;

    @Column(name = "main")
    private boolean main;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Project project;

    @Column(name = "image")
    private byte[] image;

    /** Protected */ ProjectImage() {
        /** For hibernate only */
    }

    public ProjectImage(Project project, byte[] image, boolean main) {
        this.project = project;
        this.image = image;
        this.main = main;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }



}