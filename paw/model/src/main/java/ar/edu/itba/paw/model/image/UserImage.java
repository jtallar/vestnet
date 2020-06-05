package ar.edu.itba.paw.model.image;

import ar.edu.itba.paw.model.User;

import javax.persistence.*;

@Entity
@Table(name = "user_images")
public class UserImage {

    @Id
    @Column(name = "id")
    private long id;

    @OneToOne
    @MapsId
    private User user;

    @Column(name = "image")
    private byte[] image;

    /** Protected */ UserImage() {
        /** For hibernate only */
    }

    public UserImage(User user, byte[] image) {
        this.user = user;
        this.image = image;
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
