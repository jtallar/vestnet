package ar.edu.itba.paw.interfaces.daos;

import ar.edu.itba.paw.model.location.City;
import ar.edu.itba.paw.model.location.Country;
import ar.edu.itba.paw.model.location.State;

import java.util.List;

public interface LocationDao {

    /**
     * Gets the list of all countries.
     * @return The list of all the countries.
     */
    List<Country> findAllCountries();


    /**
     * Gets all the states for a given country.
     * @param parent The parent country.
     * @return The list of the country's respective states.
     */
    List<State> findStates(Country parent);


    /**
     * Gets all the cities for a given state.
     * @param parent The parent state.
     * @return The list of the state's respective cities.
     */
    List<City> findCities(State parent);
}
