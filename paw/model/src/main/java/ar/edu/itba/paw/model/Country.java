package ar.edu.itba.paw.model;

import org.springframework.core.annotation.Order;

import javax.persistence.*;

/**
 * Model of a Country.
 */
@Entity
@Table(name = "countries")
public class Country {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "country", length = 50, nullable = false)
    private String name;

    @Column(name = "iso2", length = 2)
    private String isoCode;

    @Column(name = "phonecode", length = 10)
    private String phoneCode;

    @Column(name = "currency", length = 10)
    private String currency;

    @Column(name = "locale", length = 5)
    private String locale;

    /** Protected */ Country() {
        /** For hibernate only */
    }

    public Country(int id, String name, String isoCode, String phoneCode, String currency, String locale) {
        this.id = id;
        this.name = name;
        this.isoCode = isoCode;
        this.phoneCode = phoneCode;
        this.currency = currency;
        this.locale = locale;
    }

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

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Override
    public String toString() {
        return "Country{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isoCode='" + isoCode + '\'' +
                ", phoneCode='" + phoneCode + '\'' +
                ", currency='" + currency + '\'' +
                ", locale='" + locale + '\'' +
                '}';
    }
}