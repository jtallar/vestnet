package ar.edu.itba.paw.webapp.dto.location;

import ar.edu.itba.paw.model.location.Country;

public class CountryDto {

    private int id;
    private String name;
    private String isoCode;
    private String phoneCode;
    private String currency;
    private String locale;

    public static CountryDto fromCountry(Country country) {
        final CountryDto countryDto = new CountryDto();

        countryDto.setId(country.getId());
        countryDto.setName(country.getName());
        countryDto.setIsoCode(country.getIsoCode());
        countryDto.setPhoneCode(country.getPhoneCode());
        countryDto.setCurrency(country.getCurrency());
        countryDto.setLocale(country.getLocale());

        return countryDto;
    }

    /* Getters and setters */

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
}
