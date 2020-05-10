package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.User;
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

import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class ProjectJdbcDaoTest {

    private static final String PROJECTS_TABLE = "projects";
    private static final String COUNTRIES_TABLE = "countries";
    private static final String STATES_TABLE = "states";
    private static final String CITIES_TABLE = "cities";
    private static final String ROLES_TABLE = "roles";
    private static final String USERS_TABLE = "users";
    private static final String CATEGORIES_TABLE = "categories";
    private static final String PROJECT_CATEGORY_TABLE = "project_categories";

    private static final int COUNTRY_ID = 1;
    private static final int STATE_ID = 2;
    private static final int CITY_ID = 3;
    private static final String FIRST_NAME = "FirstName";
    private static final String LAST_NAME = "LastName";
    private static final String REAL_ID = "00-0000000000-0";
    private static final String EMAIL = "test@test.com";

    private static final String PROJECT_NAME = "Project Name.", PROJECT_SUMMARY = "Project Summary.";
    private static final String PROJECT_NAME_2 = "Project 2", PROJECT_SUMMARY_2 = "Summary 2";
    private static final String CATEGORY_NAME = "Technology";
    private static final String CATEGORY_NAME_2 = "Software";

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ProjectJdbcDao projectJdbcDao;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsertProject, jdbcInsertUser, jdbcInsertCategory, jdbcInsertProjectCategory;
    private SimpleJdbcInsert jdbcInsertCountry, jdbcInsertState, jdbcInsertCity, jdbcInsertRole;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsertProject = new SimpleJdbcInsert(dataSource)
                .withTableName(PROJECTS_TABLE)
                .usingGeneratedKeyColumns("id");
        jdbcInsertUser = new SimpleJdbcInsert(dataSource)
                .withTableName(USERS_TABLE)
                .usingGeneratedKeyColumns("id");
        jdbcInsertCategory = new SimpleJdbcInsert(dataSource)
                .withTableName(CATEGORIES_TABLE)
                .usingGeneratedKeyColumns("id");
        jdbcInsertProjectCategory = new SimpleJdbcInsert(dataSource)
                .withTableName(PROJECT_CATEGORY_TABLE);
        jdbcInsertCountry = new SimpleJdbcInsert(dataSource)
                .withTableName(COUNTRIES_TABLE);
        jdbcInsertState = new SimpleJdbcInsert(dataSource)
                .withTableName(STATES_TABLE);
        jdbcInsertCity = new SimpleJdbcInsert(dataSource)
                .withTableName(CITIES_TABLE);
        jdbcInsertRole = new SimpleJdbcInsert(dataSource)
                .withTableName(ROLES_TABLE);

        JdbcTestUtils.deleteFromTables(jdbcTemplate, USERS_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, PROJECTS_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, CITIES_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, STATES_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, COUNTRIES_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, ROLES_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, CATEGORIES_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, PROJECT_CATEGORY_TABLE);

        createLocation();
        createRole();
    }

    @Test
    public void testCreate() {
        // 1 - Setup- Create 1 user
        Number userId = createUser();

        // 2 - Execute
        projectJdbcDao.create(PROJECT_NAME, PROJECT_SUMMARY, 0, userId.longValue(), new ArrayList<>(), new ArrayList<>(), new byte[0]);

        // 3 - Assert
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, PROJECTS_TABLE));
    }

    @Test
    public void testFindByIdDoesntExists() {
        // 1 - Setup - Empty table

        // 2 - Execute
        Optional<Project> maybeProject = projectJdbcDao.findById(1);

        // 3 - Assert
        assertFalse(maybeProject.isPresent());
    }

    @Test
    public void testFindByIdProjectExists() {
        // 1 - Setup - Create a project with a category
        Number categoryId = createCategory();
        Number projectId = createProject(createUser());
        createProjectCategory(projectId, categoryId);

        // 2 - Execute
        Optional<Project> maybeProject = projectJdbcDao.findById(projectId.longValue());

        // 3 - Assert - Name, Summary, Category
        assertTrue(maybeProject.isPresent());
        assertEquals(PROJECT_NAME, maybeProject.get().getName());
        assertEquals(PROJECT_SUMMARY, maybeProject.get().getSummary());
        assertEquals(categoryId.longValue(), maybeProject.get().getCategories().get(0).getId());
    }

    @Test
    public void testFindByOwnerDoesntExists() {
        // 1 - Setup - Empty table

        // 2 - Execute
        List<Project> projects = projectJdbcDao.findByOwner(1);

        // 3 - Assert
        assertTrue(projects.isEmpty());
    }

    @Test
    public void testFindByOwnerProjectExists() {
        // 1 - Setup - Create a project with a category
        Number userId = createUser();
        Number categoryId = createCategory();
        Number projectId = createProject(userId);
        createProjectCategory(projectId, categoryId);

        // 2 - Execute
        List<Project> projects = projectJdbcDao.findByOwner(userId.longValue());

        // 3 - Assert - Name, Summary, Category
        assertEquals(1, projects.size());
        assertEquals(PROJECT_NAME, projects.get(0).getName());
        assertEquals(PROJECT_SUMMARY, projects.get(0).getSummary());
        assertEquals(categoryId.longValue(), projects.get(0).getCategories().get(0).getId());
    }


    @Test
    public void testFindByCategoriesNull() {
        // 1 - Setup - Create user, project, category, and link them
        Number userId = createUser();
        Number categoryId = createCategory();
        Number projectId = createProject(userId);
        createProjectCategory(projectId, categoryId);

        // 2 - Execute
        List<Project> projects = projectJdbcDao.findByCategoryPage(null, 0,1, 0, Integer.MAX_VALUE);

        // 3 - Assert
        assertEquals(0, projects.size());
    }


    /**
     * Auxiliary functions
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
        role.put("id", User.UserRole.ENTREPRENEUR.getId());
        role.put("user_role", User.UserRole.ENTREPRENEUR.getRole());
        jdbcInsertRole.execute(role);
    }

    /**
     * Creates a user and inserts it to the database.
     * @return The unique generated user id.
     */
    private Number createUser() {
        Map<String, Object> user = new HashMap<>();
        user.put("role_id", User.UserRole.ENTREPRENEUR.getId());
        user.put("first_name", FIRST_NAME);
        user.put("last_name", LAST_NAME);
        user.put("real_id", REAL_ID);
        user.put("country_id", COUNTRY_ID);
        user.put("state_id", STATE_ID);
        user.put("city_id", CITY_ID);
        user.put("aux_date", new Date());
        user.put("email", EMAIL);
        return jdbcInsertUser.executeAndReturnKey(user);
    }

    /**
     * Creates a category.
     * @return The category given id.
     */
    private Number createCategory() {
        Map<String, String> category = new HashMap<String, String>();
        category.put("category", CATEGORY_NAME);
        return jdbcInsertCategory.executeAndReturnKey(category);
    }

    /**
     * Creates a project given its user id.
     * @param userId Owner user id.
     * @return The unique project id.
     */
    private Number createProject(Number userId) {
        Map<String, Object> project = new HashMap<>();
        project.put("owner_id", userId);
        project.put("project_name", PROJECT_NAME);
        project.put("summary", PROJECT_SUMMARY);
        return jdbcInsertProject.executeAndReturnKey(project);
    }

    private void createProjectCategory(Number projectId, Number categoryId) {
        Map<String, Long> values2 = new HashMap<String, Long>();
        values2.put("category_id", categoryId.longValue());
        values2.put("project_id", projectId.longValue());
        jdbcInsertProjectCategory.execute(values2);
    }

}
