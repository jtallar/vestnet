package ar.edu.itba.paw.persistence;


import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class UserJdbcDaoTest {

//    private static final String USERS_TABLE = "users";
//    private static final String COUNTRIES_TABLE = "countries";
//    private static final String STATES_TABLE = "states";
//    private static final String CITIES_TABLE = "cities";
//    private static final String ROLES_TABLE = "roles";
//
//    private static final int COUNTRY_ID = 1;
//    private static final int STATE_ID = 2;
//    private static final int CITY_ID = 3;
//
//    private static final String ROLE_NAME = User.UserRole.INVESTOR.getRole();
//    private static final String FIRST_NAME = "FirstName";
//    private static final String LAST_NAME = "LastName";
//    private static final String REAL_ID = "00-0000000000-0";
//    private static final String EMAIL = "test@test.com";
//
//    @Autowired
//    private DataSource ds;
//
//    @Autowired
//    private UserJpaDao userJdbcDao;
//    private JdbcTemplate jdbcTemplate;
//    private SimpleJdbcInsert jdbcInsertUser, jdbcInsertCountry, jdbcInsertState, jdbcInsertCity, jdbcInsertRole;
//
//    @Before
//    public void setUp() {
//        jdbcTemplate = new JdbcTemplate(ds);
//        jdbcInsertUser = new SimpleJdbcInsert(ds)
//                .withTableName(USERS_TABLE)
//                .usingGeneratedKeyColumns("id");
//        jdbcInsertCountry = new SimpleJdbcInsert(ds)
//                .withTableName(COUNTRIES_TABLE);
//        jdbcInsertState = new SimpleJdbcInsert(ds)
//                .withTableName(STATES_TABLE);
//        jdbcInsertCity = new SimpleJdbcInsert(ds)
//                .withTableName(CITIES_TABLE);
//        jdbcInsertRole = new SimpleJdbcInsert(ds)
//                .withTableName(ROLES_TABLE);
//
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, USERS_TABLE);
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, CITIES_TABLE);
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, STATES_TABLE);
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, COUNTRIES_TABLE);
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, ROLES_TABLE);
//
//        createLocation();
//        createRole();
//    }
//
//   @Test
//    public void testCreate() {
//        // 1 - Setup
//        Location location = assignLocation();
//
//        // 2 - Execute
//        try {
//            userJdbcDao.create(ROLE_NAME, FIRST_NAME, LAST_NAME, REAL_ID, LocalDate.now(), location, EMAIL, null, null, null, new byte[]{1});
//        } catch (UserAlreadyExistsException e) {
//            fail();
//        }
//
//        // 3 - Assert
//        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, USERS_TABLE));
//    }
//
//    @Test
//    public void testFindByEmailDoesntExists() {
//        // 1 - Setup - Empty table
//
//        // 2 - Execute
//        Optional<User> optionalUser = userJdbcDao.findByUsername(EMAIL);
//
//        // 3 - Assert
//        assertFalse(optionalUser.isPresent());
//    }
//
//    @Test
//    public void testFindByEmailUserExists() {
//        // 1 - Setup - Create 1 user
//        createUser();
//
//        // 2 - Execute
//        Optional<User> optionalUser = userJdbcDao.findByUsername(EMAIL);
//
//        // 3 - Assert - FIRST, LAST NAME
//        assertTrue(optionalUser.isPresent());
//        assertEquals(FIRST_NAME, optionalUser.get().getFirstName());
//        assertEquals(LAST_NAME, optionalUser.get().getLastName());
//        assertEquals(EMAIL, optionalUser.get().getEmail());
//    }
//
//    @Test
//    public void testFindByIdDoesntExists() {
//        // 1 - Setup - Empty table
//
//        // 2 - Execute
//        Optional<User> optionalUser = userJdbcDao.findById(1);
//
//        // 3 - Assert
//        assertFalse(optionalUser.isPresent());
//    }
//
//    @Test
//    public void testFindByIdUserExists() {
//        // 1 - Setup - Create 1 user
//        Number userId = createUser();
//
//        // 2 - Execute
//        Optional<User> optionalUser = userJdbcDao.findById(userId.longValue());
//
//        // 3 - Assert - FIRST, LAST NAME
//        assertTrue(optionalUser.isPresent());
//        assertEquals(FIRST_NAME, optionalUser.get().getFirstName());
//        assertEquals(LAST_NAME, optionalUser.get().getLastName());
//    }
//
//
//    /**
//     * Auxiliary functions.
//     */
//
//    /**
//     * Creates a location.
//     */
//    private void createLocation() {
//        Map<String, Object> country = new HashMap<>();
//        country.put("id", COUNTRY_ID);
//        country.put("country", "Peronlandia");
//        jdbcInsertCountry.execute(country);
//
//        Map<String, Object> state = new HashMap<>();
//        state.put("id", STATE_ID);
//        state.put("state", "Buenos Aires");
//        state.put("country_id", COUNTRY_ID);
//        jdbcInsertState.execute(state);
//
//        Map<String, Object> city = new HashMap<>();
//        city.put("id", CITY_ID);
//        city.put("city", "La Matanza");
//        city.put("state_id", STATE_ID);
//        jdbcInsertCity.execute(city);
//    }
//
//    /**
//     * Creates a user role.
//     */
//    private void createRole() {
//        Map<String, Object> role = new HashMap<>();
//        role.put("id", User.UserRole.INVESTOR.getId());
//        role.put("user_role", User.UserRole.INVESTOR.getRole());
//        jdbcInsertRole.execute(role);
//    }
//
//    /**
//     * Assigns the default location to a Location.
//     * @return The Location.
//     */
//    private Location assignLocation() {
//        return new Location(new Location.Country(COUNTRY_ID, null, null, null, null, null),
//                new Location.State(STATE_ID, null, null),
//                new Location.City(CITY_ID, null));
//    }
//
//    /**
//     * Creates a user and inserts it to the database.
//     * @return The unique generated user id.
//     */
//    private Number createUser() {
//        Map<String, Object> user = new HashMap<>();
//        user.put("role_id", User.UserRole.INVESTOR.getId());
//        user.put("first_name", FIRST_NAME);
//        user.put("last_name", LAST_NAME);
//        user.put("real_id", REAL_ID);
//        user.put("country_id", COUNTRY_ID);
//        user.put("state_id", STATE_ID);
//        user.put("city_id", CITY_ID);
//        user.put("aux_date", new Date());
//        user.put("email", EMAIL);
//        return jdbcInsertUser.executeAndReturnKey(user);
//    }

}
