package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.components.UserRole;
import ar.edu.itba.paw.model.image.ProjectImage;
import ar.edu.itba.paw.model.image.UserImage;
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
import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class ImageJpaDaoTest {
    private static final String PROJECT_IMAGES_TABLE = "project_images";
    private static final String USER_IMAGES_TABLE = "user_images";
    private static final long IMAGE_ID = 2;

    private static final String PROJECTS_TABLE = "projects";
    private static final String COUNTRIES_TABLE = "countries";
    private static final String STATES_TABLE = "states";
    private static final String CITIES_TABLE = "cities";
    private static final String ROLES_TABLE = "roles";
    private static final String USERS_TABLE = "users";
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


    private static final String PROJECT_NAME = "Project Name.", PROJECT_SUMMARY = "Project Summary.";
    private static final long PROJECT_COST = 1200;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ImageJpaDao imageJpaDao;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsertProjectImage, jdbcInsertUserImage, jdbcInsertProject, jdbcInsertUser;
    private SimpleJdbcInsert jdbcInsertCountry, jdbcInsertState, jdbcInsertCity, jdbcInsertRole, jdbcInsertLocation;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsertProjectImage = new SimpleJdbcInsert(dataSource)
                .withTableName(PROJECT_IMAGES_TABLE);
        jdbcInsertUserImage = new SimpleJdbcInsert(dataSource)
                .withTableName(USER_IMAGES_TABLE);
        jdbcInsertProject = new SimpleJdbcInsert(dataSource)
                .withTableName(PROJECTS_TABLE)
                .usingGeneratedKeyColumns("id");
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

        JdbcTestUtils.deleteFromTables(jdbcTemplate, PROJECTS_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, USERS_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, LOCATIONS_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, CITIES_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, STATES_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, COUNTRIES_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, ROLES_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, PROJECT_IMAGES_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, USER_IMAGES_TABLE);

        createLocation();
        createRole();
        createUser();
        createProject(createUser());
    }

    @Test
    public void testCreateProjectImage() {
        // 1 - Setup- Create 1 project
        Number projectId = createProject(createUser());

        // 2 - Execute
        imageJpaDao.create(new Project(projectId.longValue()), null, true);

        // 3 - Assert
        assertEquals(1, TestUtils.countRowsInTable(entityManager, PROJECT_IMAGES_TABLE));
    }

    @Test
    public void testCreateUserImage() {
        // 1 - Setup- Nothing to be done

        // 2 - Execute
        imageJpaDao.create(null);

        // 3 - Assert
        assertEquals(1, TestUtils.countRowsInTable(entityManager, USER_IMAGES_TABLE));
    }

    @Test
    public void testFindProjectMainImagesEmpty() {
        // 1 - Setup- Create a project
        Number projectId = createProject(createUser());

        // 2 - Execute
        List<ProjectImage> mainImage = imageJpaDao.findProjectImages(new Project(projectId.longValue()), true);

        // 3 - Assert
        assertEquals(0, mainImage.size());
    }

    @Test
    public void testFindProjectMainImagesExists() {
        // 1 - Setup- Create a project
        Number projectId = createProject(createUser());
        createProjectImage(projectId, true);

        // 2 - Execute
        List<ProjectImage> mainImage = imageJpaDao.findProjectImages(new Project(projectId.longValue()), true);

        // 3 - Assert
        assertEquals(1, mainImage.size());
    }

    @Test
    public void testFindProjectNotMainImagesExist() {
        // 1 - Setup- Create a project
        Number projectId = createProject(createUser());
        createProjectImage(projectId, false);

        // 2 - Execute
        List<ProjectImage> mainImage = imageJpaDao.findProjectImages(new Project(projectId.longValue()), false);

        // 3 - Assert
        assertEquals(1, mainImage.size());
    }

    @Test
    public void testFindUserImageEmpty() {
        // 1 - Setup- Create a user
        Number userId = createUser();

        // 2 - Execute
        Optional<UserImage> userImage = imageJpaDao.findUserImage(userId.longValue());

        // 3 - Assert
        assertFalse(userImage.isPresent());
    }

    @Test
    public void testFindUserImageExists() {
        // 1 - Setup- Create a user
        Number userId = createUser();
        createUserImage(userId);

        // 2 - Execute
        Optional<UserImage> userImage = imageJpaDao.findUserImage(userId.longValue());

        // 3 - Assert
        assertTrue(userImage.isPresent());
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
     * Creates a project given its name, owner id and cost
     * @param userId Owner user id.
     * @return The unique project id.
     */
    private Number createProject(Number userId) {
        Map<String, Object> project = new HashMap<>();
        project.put("owner_id", userId.longValue());
        project.put("project_name", PROJECT_NAME);
        project.put("summary", PROJECT_SUMMARY);
        project.put("cost", PROJECT_COST);
        project.put("funded", true);
        project.put("hits", 0);
        return jdbcInsertProject.executeAndReturnKey(project);
    }

    /**
     * Create a project image
     * @param projectId Project id.
     * @param main Wheter image is main or not
     */
    private void createProjectImage(Number projectId, boolean main) {
        Map<String, Object> projectImage = new HashMap<>();
        projectImage.put("id", IMAGE_ID);
        projectImage.put("project_id", projectId.longValue());
        projectImage.put("image", null);
        projectImage.put("main", main);
        jdbcInsertProjectImage.execute(projectImage);
    }


    /**
     * Create a user image
     * @param userId User id.
     */
    private void createUserImage(Number userId) {
        Map<String, Object> userImage = new HashMap<>();
        userImage.put("id", IMAGE_ID);
        userImage.put("user_id", userId.longValue());
        userImage.put("image", null);
        jdbcInsertUserImage.execute(userImage);
    }
}
