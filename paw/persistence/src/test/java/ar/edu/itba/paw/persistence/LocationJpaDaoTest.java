package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.location.City;
import ar.edu.itba.paw.model.location.Country;
import ar.edu.itba.paw.model.location.State;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class LocationJpaDaoTest {

    private static final String COUNTRY_TABLE = "countries";
    private static final String STATE_TABLE = "states";
    private static final String CITY_TABLE = "cities";
    private static final String USERS_TABLE = "users";


    private static final String COUNTRY_NAME = "Peronlandia";
    private static final int COUNTRY_ID = 1;
    private static final String STATE_NAME = "Buenos Aires";
    private static final int STATE_ID = 2;
    private static final String CITY_NAME = "La Matanza";
    private static final int CITY_ID = 3;

    private static final int BASE_COUNTRY_ID = 0;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private LocationJpaDao locationJpaDao;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsertCountry, jdbcInsertState, jdbcInsertCity;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsertCountry = new SimpleJdbcInsert(dataSource)
                .withTableName(COUNTRY_TABLE);
        jdbcInsertState = new SimpleJdbcInsert(dataSource)
                .withTableName(STATE_TABLE);
        jdbcInsertCity = new SimpleJdbcInsert(dataSource)
                .withTableName(CITY_TABLE);

        // Prevent from triggering ON DELETE RESTRICT on COUNTRY DELETE
        JdbcTestUtils.deleteFromTables(jdbcTemplate, USERS_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, COUNTRY_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, STATE_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, CITY_TABLE);

        createBaseCountry();
    }

    @Test
    public void testFindAllCountriesIfTableEmpty() {
        // 1 - Setup - Empty country table

        // 2 - Execute
        List<Country> countries = locationJpaDao.findAllCountries();

        // 3 - Assert
        assertTrue(countries.isEmpty());
    }

    @Test
    public void testFindAllCountriesIfTableNotEmpty() {
        // 1 - Setup - Insert 1 country
        createCountry();

        // 2 - Execute
        List<Country> countries = locationJpaDao.findAllCountries();

        // 3 - Assert - Quantity, Name, ID
        assertEquals(1, countries.size());
        assertEquals(COUNTRY_NAME, countries.get(0).getName());
        assertEquals(COUNTRY_ID, countries.get(0).getId());
    }

    @Test
    public void testFindStateIfTableEmpty() {
        // 1 - Setup - Empty tables

        // 2 - Execute
        List<State> states = locationJpaDao.findStates(new Country(COUNTRY_ID));

        // 3 - Assert
        assertTrue(states.isEmpty());
    }

    @Test
    public void testFindStateIfTableNotEmpty() {
        // 1 - Setup - Add 1 country and 1 state
        createCountry();
        createState();

        // 2 - Execute
        List<State> states = locationJpaDao.findStates(new Country(COUNTRY_ID));

        // 3 - Assert - Quantity, Name, ID
        assertEquals(1, states.size());
        assertEquals(STATE_NAME, states.get(0).getName());
        assertEquals(STATE_ID, states.get(0).getId());
    }

    @Test
    public void testFindCityIfTableEmpty() {
        // 1 - Setup - Empty tables

        // 2 - Execute
        List<City> city = locationJpaDao.findCities(new State(STATE_ID));

        // 3 - Assert
        assertTrue(city.isEmpty());
    }

    @Test
    public void testFindCityIfTableNotEmpty() {
        // 1 - Setup - Add 1 country, 1 state and 1 city
        createCountry();
        createState();
        createCity();

        // 2 - Execute
        List<City> city = locationJpaDao.findCities(new State(STATE_ID));

        // 3 - Assert - Quantity, Name, ID
        assertEquals(1, city.size());
        assertEquals(CITY_NAME, city.get(0).getName());
        assertEquals(CITY_ID, city.get(0).getId());
    }

    /**
     * Auxiliary functions
     */

    /**
     * Creates a base country to use when no states/cities are found.
     */
    private void createBaseCountry() {
        Map<String, Object> values = new HashMap<>();
        values.put("id", BASE_COUNTRY_ID);
        values.put("country", "-");
        jdbcInsertCountry.execute(values);
    }

    /**
     * Creates a country.
     */
    public void createCountry() {
        Map<String, Object> values = new HashMap<>();
        values.put("id", COUNTRY_ID);
        values.put("country", COUNTRY_NAME);
        jdbcInsertCountry.execute(values);
    }

    /**
     * Creates a state.
     */
    public void createState() {
        Map<String, Object> values = new HashMap<>();
        values.put("id", STATE_ID);
        values.put("state", STATE_NAME);
        values.put("country_id", COUNTRY_ID);
        jdbcInsertState.execute(values);
    }

    /**
     * Creates a city.
     */
    public void createCity() {
        Map<String, Object> values = new HashMap<>();
        values.put("id", CITY_ID);
        values.put("city", CITY_NAME);
        values.put("state_id", STATE_ID);
        jdbcInsertCity.execute(values);
    }

}
