package ar.edu.itba.paw.model.image;

import ar.edu.itba.paw.model.User;

import javax.persistence.*;

@Entity
@Table(name = "user_images")
public class UserImage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_images_id_seq")
    @SequenceGenerator(sequenceName = "user_images_id_seq", name = "user_images_id_seq", allocationSize = 1)
    @Column(name = "id")
    private long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false, mappedBy = "image")
    private User user;

    @Column(name = "image")
    private byte[] image;

    /** Protected */ UserImage() {
        /** For hibernate only */
    }

    public UserImage(byte[] image) {
        this.image = image;
    }

    public UserImage(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
