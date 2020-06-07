package ar.edu.itba.paw.model.location;

import javax.persistence.*;

/**
 * Model of a State.
 */
@Entity
@Table(name = "states")
public class State {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "state", length = 75, nullable = false)
    private String name;

    @Column(name = "iso2", length = 10)
    private String isoCode;

    /** Protected */ State() {
        /** For hibernate only */
    }

    public State(int id, String name, String isoCode) {
        this.id = id;
        this.name = name;
        this.isoCode = isoCode;
    }

    public State(int id) {
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

    public String getIsoCode() {
        return isoCode;
    }

    public void setIsoCode(String isoCode) {
        this.isoCode = isoCode;
    }


    @Override
    public String toString() {
        return "State{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isoCode='" + isoCode + '\'' +
                '}';
    }
}
