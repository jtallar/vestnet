package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.Location;
import ar.edu.itba.paw.model.Location.City;
import ar.edu.itba.paw.model.Location.Country;
import ar.edu.itba.paw.model.Location.State;

import java.util.List;

public interface LocationDao {

    /**
     * Gets the list of all countries.
     * @return The list of all the countries.
     */
    List<Country> findAllCountries();

    /**
     * Gets all the states for a given country.
     * @param country_id The unique id of the country.
     * @return The list of the country's respective states.
     */
    List<State> findStates(long country_id);

    /**
     * Gets all the cities for a given state.
     * @param state_id The unique id of the state.
     * @return The list of the state's respective cities.
     */
    List<City> findCities(long state_id);
}
