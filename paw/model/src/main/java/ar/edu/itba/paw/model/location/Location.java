package ar.edu.itba.paw.model.location;

import ar.edu.itba.paw.model.User;

import javax.persistence.*;

/**
 * Models a location object with Country, State and City.
 */
@Entity
@Table(name = "user_location")
public class Location {

    @Id
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Country country;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private State state;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private City city;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    private User user;

    /** Protected */ Location() {
        /** For hibernate only*/
    }

    public Location(Country country, State state, City city) {
        this.country = country;
        this.state = state;
        this.city = city;
    }



    /** Getters and setters */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Location{" +
                "country=" + country +
                ", state=" + state +
                ", city=" + city +
                '}';
    }
}
