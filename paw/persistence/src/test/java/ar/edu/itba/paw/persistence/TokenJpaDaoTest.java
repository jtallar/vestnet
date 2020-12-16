package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Token;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.enums.UserRole;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.*;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class TokenJpaDaoTest {
    private static final String TOKEN_TABLE = "token";
    private static final String USERS_TABLE = "users";
    private static final String COUNTRIES_TABLE = "countries";
    private static final String STATES_TABLE = "states";
    private static final String CITIES_TABLE = "cities";
    private static final String ROLES_TABLE = "roles";
    private static final String LOCATIONS_TABLE = "user_location";

    private static final Integer ROLE_ID = UserRole.INVESTOR.getId();
    private static final String FIRST_NAME = "FirstName";
    private static final String LAST_NAME = "LastName";
    private static final String REAL_ID = "00-0000000000-0";
    private static final String EMAIL = "test@test.com";
    private static final String PASSWORD = "12345";
    private static final String LOCALE = "en";

    private static final String TOKEN = "this-is-actually-not-like-this";
    private static final int EXPIRATION = 60 * 24;
    private static final int TOKEN_ID = 2;

    private static final int COUNTRY_ID = 1;
    private static final int STATE_ID = 2;
    private static final int CITY_ID = 3;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private TokenJpaDao tokenJpaDao;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsertUser, jdbcInsertRole, jdbcInsertToken;
    private SimpleJdbcInsert jdbcInsertCountry, jdbcInsertState, jdbcInsertCity, jdbcInsertLocation;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsertToken = new SimpleJdbcInsert(dataSource)
                .withTableName(TOKEN_TABLE);
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

        JdbcTestUtils.deleteFromTables(jdbcTemplate, TOKEN_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, USERS_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, CITIES_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, STATES_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, COUNTRIES_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, ROLES_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, LOCATIONS_TABLE);

        createLocation();
        createRole();
        createUser();
    }

    @Test
    public void testCreate() {
        // 1 - Setup
        Number userId = createUser();

        // 2 - Execute
        tokenJpaDao.create(new User(userId.longValue()));

        // 3 - Assert
        assertEquals(1, TestUtils.countRowsInTable(entityManager, TOKEN_TABLE));
    }

    @Test
    public void testFindByTokenDoesntExists() {
        // 1 - Setup - Empty table

        // 2 - Execute
        Optional<Token> optionalToken = tokenJpaDao.findByToken(TOKEN);

        // 3 - Assert
        assertFalse(optionalToken.isPresent());
    }

    @Test
    public void testFindByTokenExists() {
        // 1 - Setup - Create 1 user
        Number userId = createToken();

        // 2 - Execute
        Optional<Token> optionalToken = tokenJpaDao.findByToken(TOKEN);

        // 3 - Assert - FIRST, LAST NAME
        assertTrue(optionalToken.isPresent());
        assertEquals(TOKEN, optionalToken.get().getToken());
        assertTrue(optionalToken.get().isValid());
        assertEquals(userId.longValue(), optionalToken.get().getUser().getId());
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

    /**
     * Creates a new token and inserts it
     */
    private Number createToken() {
        Number userId = createUser();
        Map<String, Object> token = new HashMap<>();
        token.put("id", TOKEN_ID);
        token.put("user_id", userId);
        token.put("token", TOKEN);
        token.put("expirydate", calculateExpiryDate(EXPIRATION));
        jdbcInsertToken.execute(token);
        return userId;
    }

    /**
     * Calculates expiry date given a time.
     * @param expiryTimeInMinutes The time given to calculate.
     * @return Date of expiry.
     */
    private Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

}
