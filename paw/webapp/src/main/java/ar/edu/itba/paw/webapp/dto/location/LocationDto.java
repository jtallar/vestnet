package ar.edu.itba.paw.webapp.dto.location;

import ar.edu.itba.paw.model.location.Location;

public class LocationDto {

    private Long id;
    private String country;

    private String state;

    private String city;



    public static LocationDto fromLocation(Location location){
        LocationDto locationDto = new LocationDto();
        locationDto.setId(location.getId());
        locationDto.setCountry(location.getCountry().getName());
        locationDto.setState(location.getState().getName());
        locationDto.setCity(location.getCity().getName());


        return  locationDto;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
