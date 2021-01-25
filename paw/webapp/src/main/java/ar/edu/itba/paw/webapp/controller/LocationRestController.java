package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.LocationService;
import ar.edu.itba.paw.webapp.dto.location.CityDto;
import ar.edu.itba.paw.webapp.dto.location.CountryDto;
import ar.edu.itba.paw.webapp.dto.location.StateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Path("/location")
public class LocationRestController {

    @Autowired
    private LocationService locationService;

    @GET
    @Path("/country")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response countryList() {

        final List<CountryDto> countries = locationService.findAllCountries().stream().map(CountryDto::fromCountry).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<CountryDto>>(countries) {}).build();
    }

    @GET
    @Path("/state/{country_id}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response stateList(@PathParam("country_id") final int countryId) {

        final List<StateDto> states = locationService.findStates(countryId).stream().map(StateDto::fromState).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<StateDto>>(states) {}).build();
    }

    @GET
    @Path("/city/{state_id}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response cityList(@PathParam("state_id") final int stateId) {

        final List<CityDto> cities = locationService.findCities(stateId).stream().map(CityDto::fromCity).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<CityDto>>(cities) {}).build();
    }
}
