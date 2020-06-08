package ar.edu.itba.paw.model.image;

import ar.edu.itba.paw.model.User;

import javax.persistence.*;

@Entity
@Table(name = "user_images")
public class UserImage extends Image {

    @OneToOne(fetch = FetchType.LAZY, optional = false, mappedBy = "image")
    private User user;

    /** Protected */ UserImage() {
        /** For hibernate only */
    }

    public UserImage(long id) {
        super(id);
    }

    public UserImage(byte[] image) {
        super(image);
    }


    /** Getters and setters */

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
