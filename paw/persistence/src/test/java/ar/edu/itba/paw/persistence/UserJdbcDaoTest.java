package ar.edu.itba.paw.persistence;


import ar.edu.itba.paw.model.Location;
import ar.edu.itba.paw.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class UserJdbcDaoTest {
    private static final String USERS_TABLE = "users";
    private static final String FN = "Jorge", LN = "Cho", RID = "100", BD = "15/02/96", EM = "hola@mail.com";

    private static final String COUNTRIES_TABLE = "countries";
    private static final String STATES_TABLE = "states";
    private static final String CITIES_TABLE = "cities";
    private static final String ROLES_TABLE = "roles";

    @Autowired
    private DataSource ds;

    @Autowired
    private UserJdbcDao userJdbcDao;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsertUser, jdbcInsertCountry, jdbcInsertState, jdbcInsertCity, jdbcInsertRole;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsertUser = new SimpleJdbcInsert(ds)
                .withTableName(USERS_TABLE)
                .usingGeneratedKeyColumns("id");
        jdbcInsertCountry = new SimpleJdbcInsert(ds)
                .withTableName(COUNTRIES_TABLE);
        jdbcInsertState = new SimpleJdbcInsert(ds)
                .withTableName(STATES_TABLE);
        jdbcInsertCity = new SimpleJdbcInsert(ds)
                .withTableName(CITIES_TABLE);
        jdbcInsertRole = new SimpleJdbcInsert(ds)
                .withTableName(ROLES_TABLE);

        JdbcTestUtils.deleteFromTables(jdbcTemplate, USERS_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, CITIES_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, STATES_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, COUNTRIES_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, ROLES_TABLE);
    }

    @Test
    public void testCreate() {
        // 1
        Map<String, Object> country = new HashMap<>();
        country.put("id", 1);
        country.put("country", "country");
        jdbcInsertCountry.execute(country);

        Map<String, Object> state = new HashMap<>();
        state.put("id", 1);
        state.put("state", "state");
        jdbcInsertState.execute(state);

        Map<String, Object> city = new HashMap<>();
        city.put("id", 1);
        city.put("city", "city");
        jdbcInsertCity.execute(city);

        Map<String, Object> role = new HashMap<>();
        role.put("id", 1);
        role.put("user_role", "User");
        jdbcInsertRole.execute(role);

        // 2
        Location location = new Location(new Location.Country(1, null, null, null, null),
                new Location.State(1, null, null), new Location.City(1, null));
        User user = userJdbcDao.create(1, FN, LN, RID, new Date(), location, EM, null, null,
                null, null, 0);

        // 3
        assertNotNull(user);
        assertEquals(FN, user.getFirstName());
        assertEquals(LN, user.getLastName());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, USERS_TABLE));
    }

    @Test
    public void testFindByIdDoesntExists() {
        // 1
        // TABLE EMPTY

        // 2
        Optional<User> maybeUser = userJdbcDao.findById(1);

        // 3
        assertFalse(maybeUser.isPresent());
    }

    // TODO: REVISIT WHEN REFACTORING USERDAO METHOD
    /*@Test
    public void testFindByIdUserExists() {
        // 1
        Map<String, Object> user = new HashMap<String, Object>();
        user.put("first_name", FN);
        user.put("last_name", LN);
        user.put("real_id", RID);
        user.put("aux_date", new Date());
        user.put("email", EM);
        Number keyNumber = jdbcInsertUser.executeAndReturnKey(user);

        // 2
        Optional<User> maybeUser = userJdbcDao.findById(keyNumber.longValue());

        // 3
        assertTrue(maybeUser.isPresent());
        assertEquals(FN, maybeUser.get().getFirstName());
        assertEquals(LN, maybeUser.get().getLastName());
    }*/
}
