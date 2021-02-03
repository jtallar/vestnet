package ar.edu.itba.paw.model.location;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Model of a City.
 */
@Entity
@Table(name = "cities")
public class City {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "city", length = 75)
    private String name;

    /** Protected */ City() {
        /* For hibernate only */
    }

    public City(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public City(int id) {
        this.id = id;
    }

    /** Getters and setters */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}