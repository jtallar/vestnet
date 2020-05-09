package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.Location;

import java.util.List;

public interface LocationService {
    /**
     * Gets the list of all countries.
     * @return The list of all the countries.
     */
    List<Location.Country> findAllCountries();

    /**
     * Gets all the states for a given country.
     * @param countryId The unique id of the country.
     * @return The list of the country's respective states.
     */
    List<Location.State> findStates(long countryId);

    /**
     * Gets all the cities for a given state.
     * @param stateId The unique id of the state.
     * @return The list of the state's respective cities.
     */
    List<Location.City> findCities(long stateId);
}
