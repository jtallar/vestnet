package ar.edu.itba.paw.model.image;

import javax.persistence.*;

@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "images_id_seq")
    @SequenceGenerator(sequenceName = "images_id_seq", name = "images_id_seq", allocationSize = 1, initialValue = 50)
    @Column(name = "id")
    private long id;

    @Column(name = "image")
    private byte[] image;

    /** Protected */ Image() {
        /** For hibernate only */
    }

    public Image(byte[] image) {
        this.image = image;
    }

    public Image(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
