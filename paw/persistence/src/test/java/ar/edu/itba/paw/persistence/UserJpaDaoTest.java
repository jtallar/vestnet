package ar.edu.itba.paw.persistence;


import ar.edu.itba.paw.interfaces.exceptions.UserAlreadyExistsException;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.components.UserRole;
import ar.edu.itba.paw.model.image.UserImage;
import ar.edu.itba.paw.model.location.City;
import ar.edu.itba.paw.model.location.Country;
import ar.edu.itba.paw.model.location.Location;
import ar.edu.itba.paw.model.location.State;
import org.hibernate.tuple.entity.EntityTuplizer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
@Rollback(false)
public class UserJpaDaoTest {

    private static final String USERS_TABLE = "users";
    private static final String COUNTRIES_TABLE = "countries";
    private static final String STATES_TABLE = "states";
    private static final String CITIES_TABLE = "cities";
    private static final String ROLES_TABLE = "roles";
    private static final String LOCATIONS_TABLE = "user_location";

    private static final int COUNTRY_ID = 1;
    private static final int STATE_ID = 2;
    private static final int CITY_ID = 3;

    private static final Integer ROLE_ID = UserRole.INVESTOR.getId();
    private static final String FIRST_NAME = "FirstName";
    private static final String LAST_NAME = "LastName";
    private static final String REAL_ID = "00-0000000000-0";
    private static final String EMAIL = "test@test.com";
    private static final String PASSWORD = "12345";
    private static final String LOCALE = "en";

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserJpaDao userJdbcDao;


    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsertUser, jdbcInsertCountry, jdbcInsertState, jdbcInsertCity, jdbcInsertRole, jdbcInsertLocation;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsertUser = new SimpleJdbcInsert(dataSource)
                .withTableName(USERS_TABLE)
                .usingGeneratedKeyColumns("id");
        jdbcInsertCountry = new SimpleJdbcInsert(dataSource)
                .withTableName(COUNTRIES_TABLE);
        jdbcInsertState = new SimpleJdbcInsert(dataSource)
                .withTableName(STATES_TABLE);
        jdbcInsertCity = new SimpleJdbcInsert(dataSource)
                .withTableName(CITIES_TABLE);
        jdbcInsertRole = new SimpleJdbcInsert(dataSource)
                .withTableName(ROLES_TABLE);
        jdbcInsertLocation = new SimpleJdbcInsert(dataSource)
                .usingGeneratedKeyColumns("id")
                .withTableName(LOCATIONS_TABLE);

        JdbcTestUtils.deleteFromTables(jdbcTemplate, USERS_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, CITIES_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, STATES_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, COUNTRIES_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, ROLES_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, LOCATIONS_TABLE);

        createLocation();
        createRole();
    }


    @Test
    public void testCreate() {
        // 1 - Setup
        Location location = assignLocation();

        // 2 - Execute
        try {
            userJdbcDao.create(ROLE_ID, PASSWORD ,FIRST_NAME, LAST_NAME, REAL_ID, new Date(), location, EMAIL, null, null, null);
        } catch (UserAlreadyExistsException e) {
            fail();
        }

        // 3 - Assert
        assertEquals(1, TestUtils.countRowsInTable(entityManager, USERS_TABLE));
    }

    @Test
    public void testFindByEmailDoesntExists() {
        // 1 - Setup - Empty table

        // 2 - Execute
        Optional<User> optionalUser = userJdbcDao.findByUsername(EMAIL);

        // 3 - Assert
        assertFalse(optionalUser.isPresent());
    }

    @Test
    public void testFindByEmailUserExists() {
        // 1 - Setup - Create 1 user
        createUser();

        // 2 - Execute
        Optional<User> optionalUser = userJdbcDao.findByUsername(EMAIL);

        // 3 - Assert - FIRST, LAST NAME
        assertTrue(optionalUser.isPresent());
        assertEquals(FIRST_NAME, optionalUser.get().getFirstName());
        assertEquals(LAST_NAME, optionalUser.get().getLastName());
        assertEquals(EMAIL, optionalUser.get().getEmail());
    }

    @Test
    public void testFindByIdDoesntExists() {
        // 1 - Setup - Empty table

        // 2 - Execute
        Optional<User> optionalUser = userJdbcDao.findById(1L);

        // 3 - Assert
        assertFalse(optionalUser.isPresent());
    }

    @Test
    public void testFindByIdUserExists() {
        // 1 - Setup - Create 1 user
        Number userId = createUser();

        // 2 - Execute
        Optional<User> optionalUser = userJdbcDao.findById(userId.longValue());

        // 3 - Assert - FIRST, LAST NAME
        assertTrue(optionalUser.isPresent());
        assertEquals(FIRST_NAME, optionalUser.get().getFirstName());
        assertEquals(LAST_NAME, optionalUser.get().getLastName());
    }


    /**
     * Auxiliary functions.
     */

    /**
     * Creates a location.
     */
    private void createLocation() {
        Map<String, Object> country = new HashMap<>();
        country.put("id", COUNTRY_ID);
        country.put("country", "Peronlandia");
        jdbcInsertCountry.execute(country);

        Map<String, Object> state = new HashMap<>();
        state.put("id", STATE_ID);
        state.put("state", "Buenos Aires");
        state.put("country_id", COUNTRY_ID);
        jdbcInsertState.execute(state);

        Map<String, Object> city = new HashMap<>();
        city.put("id", CITY_ID);
        city.put("city", "La Matanza");
        city.put("state_id", STATE_ID);
        jdbcInsertCity.execute(city);
    }

    /**
     * Creates a user role.
     */
    private void createRole() {
        Map<String, Object> role = new HashMap<>();
        role.put("id", UserRole.INVESTOR.getId());
        role.put("user_role", UserRole.INVESTOR.getRole());
        jdbcInsertRole.execute(role);
    }

    /**
     * Assigns the default location to a Location.
     * @return The Location.
     */
    private Location assignLocation() {
        return new Location(new Country(COUNTRY_ID),
                new State(STATE_ID),
                new City(CITY_ID));
    }


    /**
     * Inserts a User location un DB.
     * @return The generated ID.
     */
    private Number createUserLocation() {
        Map<String, Object> location = new HashMap<>();
        location.put("country_id", COUNTRY_ID);
        location.put("state_id", STATE_ID);
        location.put("city_id", CITY_ID);
        return jdbcInsertLocation.executeAndReturnKey(location);
    }

    /**
     * Creates a user and inserts it to the database.
     * @return The unique generated user id.
     */
    private Number createUser() {

        Map<String, Object> user = new HashMap<>();
        user.put("password", PASSWORD);
        user.put("role_id", UserRole.INVESTOR.getId());
        user.put("real_id", REAL_ID);
        user.put("first_name", FIRST_NAME);
        user.put("last_name", LAST_NAME);
        user.put("location_id", createUserLocation());
        user.put("email", EMAIL);
        user.put("verified", false);
        user.put("locale", LOCALE);
        user.put("aux_date", new Date());
        return jdbcInsertUser.executeAndReturnKey(user);
    }

}
