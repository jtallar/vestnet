package ar.edu.itba.paw.webapp.dto.location;

import ar.edu.itba.paw.model.location.City;

public class CityDto {

    private int id;
    private String name;

    public static CityDto fromCity(City city) {
        final CityDto cityDto = new CityDto();

        cityDto.setId(city.getId());
        cityDto.setName(city.getName());

        return cityDto;
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
}
