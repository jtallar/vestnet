package ar.edu.itba.paw.model;

import jdk.nashorn.internal.ir.annotations.Reference;

import javax.persistence.*;

/**
 * Models a location object with Country, State and City.
 */
@Embeddable
public class Location {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Country country;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private State state;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private City city;

    /** Protected */ Location() {
        /** For hibernate only*/
    }

    public Location(Country country, State state, City city) {
        this.country = country;
        this.state = state;
        this.city = city;
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

    @Override
    public String toString() {
        return "Location{" +
                "country=" + country +
                ", state=" + state +
                ", city=" + city +
                '}';
    }
}
