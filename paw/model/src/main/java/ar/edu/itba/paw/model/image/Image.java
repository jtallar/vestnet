package ar.edu.itba.paw.model.image;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

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
        /* For hibernate only */
    }


    /** Getters and setters */

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Image)) return false;
        Image imageOb = (Image) o;
        return id == imageOb.id && Arrays.equals(image, imageOb.image);
    }

    @Override
    public int hashCode() {
        return 31 * Long.hashCode(id) + Arrays.hashCode(image);
    }
}
