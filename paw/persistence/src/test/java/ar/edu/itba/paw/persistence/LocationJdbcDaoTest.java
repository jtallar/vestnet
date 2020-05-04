package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Category;
import ar.edu.itba.paw.model.Location;
import ar.edu.itba.paw.model.Location.Country;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class LocationJdbcDaoTest {
    private static final String COUNTRY_TABLE = "countries";
    private static final String STATE_TABLE = "states";
    private static final String CITY_TABLE = "cities";

    @Autowired
    private DataSource dataSource;

    @Autowired
    private LocationJdbcDao locationJdbcDao;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsertCountry, jdbcInsertState, jdbcInsertCity;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsertCountry = new SimpleJdbcInsert(dataSource)
                .withTableName(COUNTRY_TABLE)
                .usingGeneratedKeyColumns("id");
        jdbcInsertState = new SimpleJdbcInsert(dataSource)
                .withTableName(STATE_TABLE)
                .usingGeneratedKeyColumns("id");
        jdbcInsertCity = new SimpleJdbcInsert(dataSource)
                .withTableName(CITY_TABLE);

        JdbcTestUtils.deleteFromTables(jdbcTemplate, COUNTRY_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, STATE_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, CITY_TABLE);
    }

    @Test
    public void testFindAllIfTableEmpty() {
        // 1: Precondiciones: Setup del escenario para que nuestro test corra
        // TABLE EMPTY

        // 2: Ejercitacion: Hacemos la unica llamada al metodo que queremos testear
        List<Country> countries = locationJdbcDao.findAllCountries();

        // 3: Postcondiciones: Hacemos los pocos assertes que permiten validar correctitud
        assertTrue(countries.isEmpty());
    }

}
