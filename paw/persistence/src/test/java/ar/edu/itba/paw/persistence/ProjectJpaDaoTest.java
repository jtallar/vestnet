package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.components.*;
import ar.edu.itba.paw.model.location.City;
import ar.edu.itba.paw.model.location.Country;
import ar.edu.itba.paw.model.location.Location;
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class ProjectJpaDaoTest {

    private static final String PROJECTS_TABLE = "projects";
    private static final String COUNTRIES_TABLE = "countries";
    private static final String STATES_TABLE = "states";
    private static final String CITIES_TABLE = "cities";
    private static final String ROLES_TABLE = "roles";
    private static final String USERS_TABLE = "users";
    private static final String CATEGORIES_TABLE = "categories";
    private static final String PROJECT_CATEGORY_TABLE = "project_categories";
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
    private static final String CATEGORY_NAME = "Technology";

    private static final String PROJECT_NAME_2 = "Project 2";
    private static final long PROJECT_COST_2 = 9200;
    private static final String CATEGORY_NAME_2 = "Software";

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ProjectJpaDao projectJpaDao;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsertProject, jdbcInsertUser, jdbcInsertCategory, jdbcInsertProjectCategory;
    private SimpleJdbcInsert jdbcInsertCountry, jdbcInsertState, jdbcInsertCity, jdbcInsertRole, jdbcInsertLocation;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsertProject = new SimpleJdbcInsert(dataSource)
                .withTableName(PROJECTS_TABLE)
                .usingGeneratedKeyColumns("id");
        jdbcInsertCategory = new SimpleJdbcInsert(dataSource)
                .withTableName(CATEGORIES_TABLE)
                .usingGeneratedKeyColumns("id");
        jdbcInsertProjectCategory = new SimpleJdbcInsert(dataSource)
                .withTableName(PROJECT_CATEGORY_TABLE);
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

        JdbcTestUtils.deleteFromTables(jdbcTemplate, PROJECT_CATEGORY_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, PROJECTS_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, CATEGORIES_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, USERS_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, LOCATIONS_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, CITIES_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, STATES_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, COUNTRIES_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, ROLES_TABLE);

        createLocation();
        createRole();
        createUser();
    }

    @Test
    public void testCreate() {
        // 1 - Setup- Create 1 user
        Number userId = createUser();

        // 2 - Execute
        projectJpaDao.create(PROJECT_NAME, PROJECT_SUMMARY, PROJECT_COST, new User(userId.longValue()), new ArrayList<>());

        // 3 - Assert
        assertEquals(1, TestUtils.countRowsInTable(entityManager, PROJECTS_TABLE));
    }

    @Test
    public void testFindByIdDoesntExists() {
        // 1 - Setup - Empty table

        // 2 - Execute
        Optional<Project> maybeProject = projectJpaDao.findById(1L);

        // 3 - Assert
        assertFalse(maybeProject.isPresent());
    }

    @Test
    public void testFindByIdProjectExists() {
        // 1 - Setup - Create a project with a category
        Number categoryId = createCategory(CATEGORY_NAME);
        Number projectId = createProject(PROJECT_NAME, createUser(), PROJECT_COST);
        createProjectCategory(projectId, categoryId);

        // 2 - Execute
        Optional<Project> maybeProject = projectJpaDao.findById(projectId.longValue());

        // 3 - Assert - Name, Summary, Category
        assertTrue(maybeProject.isPresent());
        assertEquals(PROJECT_NAME, maybeProject.get().getName());
        assertEquals(PROJECT_SUMMARY, maybeProject.get().getSummary());
        assertEquals(PROJECT_COST, maybeProject.get().getCost());
        assertEquals(categoryId.longValue(), maybeProject.get().getCategories().get(0).getId());
    }

    @Test
    public void testFindByOwnerDoesntExists() {
        // 1 - Setup - Empty table
        Number userId = createUser();
        List<FilterCriteria> filters = getFilterList(getOwnerMap(userId));

        // 2 - Execute
        List<Project> projects = projectJpaDao.findAll(filters, OrderField.DEFAULT);

        // 3 - Assert
        assertTrue(projects.isEmpty());
    }

    @Test
    public void testFindByOwnerProjectExists() {
        // 1 - Setup - Create a project with a category
        Number userId = createUser();
        Number categoryId = createCategory(CATEGORY_NAME);
        Number projectId = createProject(PROJECT_NAME, userId, PROJECT_COST);
        createProjectCategory(projectId, categoryId);
        List<FilterCriteria> filters = getFilterList(getOwnerMap(userId));

        // 2 - Execute
        List<Project> projects = projectJpaDao.findAll(filters, OrderField.DEFAULT);

        // 3 - Assert - Name, Summary, Category
        assertEquals(1, projects.size());
        assertEquals(PROJECT_NAME, projects.get(0).getName());
        assertEquals(PROJECT_SUMMARY, projects.get(0).getSummary());
        assertEquals(categoryId.longValue(), projects.get(0).getCategories().get(0).getId());
    }

    @Test
    public void testFindByCategory() {
        // 1 - Setup - Create 2 different projects
        Number userId = createUser();
        Number categoryId = createCategory(CATEGORY_NAME);
        Number otherCategoryId = createCategory(CATEGORY_NAME_2);
        Number projectId = createProject(PROJECT_NAME, userId, PROJECT_COST);
        createProjectCategory(projectId, categoryId);
        Number otherProjectId = createProject(PROJECT_NAME_2, userId, PROJECT_COST);
        createProjectCategory(otherProjectId, otherCategoryId);
        List<FilterCriteria> filters = getFilterList(getCategoryMap(categoryId));

        // 2 - Execute
        List<Project> projects = projectJpaDao.findAll(filters, OrderField.DATE_DESCENDING);

        // 3 - Assert
        assertEquals(1, projects.size());
        assertEquals(PROJECT_NAME, projects.get(0).getName());
        assertEquals(categoryId.longValue(), projects.get(0).getCategories().get(0).getId());
    }

    @Test
    public void testFindByRangeMin() {
        // 1 - Setup - Create 2 different projects
        Number userId = createUser();
        Number categoryId = createCategory(CATEGORY_NAME);
        Number otherCategoryId = createCategory(CATEGORY_NAME_2);
        Number projectId = createProject(PROJECT_NAME, userId, PROJECT_COST);
        createProjectCategory(projectId, categoryId);
        Number otherProjectId = createProject(PROJECT_NAME_2, userId, PROJECT_COST_2);
        createProjectCategory(otherProjectId, otherCategoryId);
        List<FilterCriteria> filters = getFilterList(getRangeMap(String.valueOf(PROJECT_COST_2), ""));

        // 2 - Execute
        List<Project> projects = projectJpaDao.findAll(filters, OrderField.COST_ASCENDING);

        // 3 - Assert
        assertEquals(1, projects.size());
        assertEquals(PROJECT_NAME_2, projects.get(0).getName());
        assertEquals(otherCategoryId.longValue(), projects.get(0).getCategories().get(0).getId());
    }

    @Test
    public void testFindByRangeMax() {
        // 1 - Setup - Create 2 different projects
        Number userId = createUser();
        Number categoryId = createCategory(CATEGORY_NAME);
        Number otherCategoryId = createCategory(CATEGORY_NAME_2);
        Number projectId = createProject(PROJECT_NAME, userId, PROJECT_COST);
        createProjectCategory(projectId, categoryId);
        Number otherProjectId = createProject(PROJECT_NAME_2, userId, PROJECT_COST_2);
        createProjectCategory(otherProjectId, otherCategoryId);
        List<FilterCriteria> filters = getFilterList(getRangeMap("", String.valueOf(PROJECT_COST)));

        // 2 - Execute
        List<Project> projects = projectJpaDao.findAll(filters, OrderField.COST_ASCENDING);

        // 3 - Assert
        assertEquals(1, projects.size());
        assertEquals(PROJECT_NAME, projects.get(0).getName());
        assertEquals(categoryId.longValue(), projects.get(0).getCategories().get(0).getId());
    }

    @Test
    public void testFindByRangeMinMax() {
        // 1 - Setup - Create 2 different projects
        Number userId = createUser();
        Number categoryId = createCategory(CATEGORY_NAME);
        Number otherCategoryId = createCategory(CATEGORY_NAME_2);
        Number projectId = createProject(PROJECT_NAME, userId, PROJECT_COST);
        createProjectCategory(projectId, categoryId);
        Number otherProjectId = createProject(PROJECT_NAME_2, userId, PROJECT_COST_2);
        createProjectCategory(otherProjectId, otherCategoryId);
        List<FilterCriteria> filters = getFilterList(getRangeMap(String.valueOf(PROJECT_COST), String.valueOf(PROJECT_COST_2)));

        // 2 - Execute
        List<Project> projects = projectJpaDao.findAll(filters, OrderField.COST_DESCENDING);

        // 3 - Assert
        assertEquals(2, projects.size());
        assertEquals(PROJECT_NAME_2, projects.get(0).getName());
        assertEquals(PROJECT_NAME, projects.get(1).getName());
    }

    @Test
    public void testFindByKeywordNameFound() {
        // 1 - Setup - Create 2 different projects
        Number userId = createUser();
        Number categoryId = createCategory(CATEGORY_NAME);
        Number otherCategoryId = createCategory(CATEGORY_NAME_2);
        Number projectId = createProject(PROJECT_NAME, userId, PROJECT_COST);
        createProjectCategory(projectId, categoryId);
        Number otherProjectId = createProject(PROJECT_NAME_2, userId, PROJECT_COST_2);
        createProjectCategory(otherProjectId, otherCategoryId);
        List<FilterCriteria> filters = getFilterList(getKeywordMap(PROJECT_NAME, SearchField.PROJECT_NAME.getValue()));

        // 2 - Execute
        List<Project> projects = projectJpaDao.findAll(filters, OrderField.DEFAULT);

        // 3 - Assert
        assertEquals(1, projects.size());
        assertEquals(PROJECT_NAME, projects.get(0).getName());
        assertEquals(categoryId.longValue(), projects.get(0).getCategories().get(0).getId());
    }

    @Test
    public void testFindByKeywordNameNotFound() {
        // 1 - Setup - Create 2 different projects
        Number userId = createUser();
        Number categoryId = createCategory(CATEGORY_NAME);
        Number otherCategoryId = createCategory(CATEGORY_NAME_2);
        Number projectId = createProject(PROJECT_NAME, userId, PROJECT_COST);
        createProjectCategory(projectId, categoryId);
        Number otherProjectId = createProject(PROJECT_NAME, userId, PROJECT_COST_2);
        createProjectCategory(otherProjectId, otherCategoryId);
        List<FilterCriteria> filters = getFilterList(getKeywordMap(PROJECT_NAME_2, SearchField.PROJECT_NAME.getValue()));

        // 2 - Execute
        List<Project> projects = projectJpaDao.findAll(filters, OrderField.DEFAULT);

        // 3 - Assert
        assertEquals(0, projects.size());
    }

    @Test
    public void testFindByMultipleFilters() {
        // 1 - Setup - Create 4 different projects
        Number userId = createUser();
        Number categoryId = createCategory(CATEGORY_NAME);
        Number otherCategoryId = createCategory(CATEGORY_NAME_2);
        Number projectId = createProject(PROJECT_NAME, userId, PROJECT_COST);
        createProjectCategory(projectId, categoryId);
        Number otherProjectId = createProject(PROJECT_NAME_2, userId, PROJECT_COST_2);
        createProjectCategory(otherProjectId, otherCategoryId);
        Number thirdProjectId = createProject(PROJECT_NAME, userId, PROJECT_COST_2);
        createProjectCategory(thirdProjectId, categoryId);
        Number fourthProjectId = createProject(PROJECT_NAME_2, userId, PROJECT_COST_2);
        createProjectCategory(fourthProjectId, categoryId);

        Map<String, Object> map = new HashMap<>();
        map.putAll(getCategoryMap(categoryId));
        map.putAll(getKeywordMap(PROJECT_NAME, SearchField.PROJECT_NAME.getValue()));
        map.putAll(getRangeMap("", String.valueOf(PROJECT_COST)));
        List<FilterCriteria> filters = getFilterList(map);

        // 2 - Execute
        List<Project> projects = projectJpaDao.findAll(filters, OrderField.ALPHABETICAL);

        // 3 - Assert
        assertEquals(1, projects.size());
        assertEquals(PROJECT_NAME, projects.get(0).getName());
        assertEquals(categoryId.longValue(), projects.get(0).getCategories().get(0).getId());
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
     * Assigns the default user to a User.
     * @return The Location.
     */
    private User assignUser() {
        return new User(ROLE_ID, PASSWORD, FIRST_NAME, LAST_NAME, REAL_ID,
                new Date(), assignLocation(), EMAIL, null, null, null);
    }

    /**
     * Creates a user and inserts it to the database.
     * @return The unique generated user id.
     */
    private Number createUser() {
        Map<String, Object> user = new HashMap<>();
        user.put("role_id", ROLE_ID);
        user.put("password", PASSWORD);
        user.put("first_name", FIRST_NAME);
        user.put("last_name", LAST_NAME);
        user.put("real_id", REAL_ID);
        user.put("country_id", COUNTRY_ID);
        user.put("state_id", STATE_ID);
        user.put("city_id", CITY_ID);
        user.put("aux_date", new Date());
        user.put("email", EMAIL);
        user.put("location_id", createUserLocation().longValue());
        user.put("locale", LOCALE);

        return jdbcInsertUser.executeAndReturnKey(user);
    }

    /**
     * Creates a category.
     * @return The category given id.
     */
    private Number createCategory(String name) {
        Map<String, String> category = new HashMap<>();
        category.put("category", name);
        return jdbcInsertCategory.executeAndReturnKey(category);
    }

    /**
     * Creates a project given its name, owner id and cost
     * @param name Project name
     * @param userId Owner user id.
     * @param cost Project cost
     * @return The unique project id.
     */
    private Number createProject(String name, Number userId, long cost) {
        Map<String, Object> project = new HashMap<>();
        project.put("owner_id", userId.longValue());
        project.put("project_name", name);
        project.put("summary", PROJECT_SUMMARY);
        project.put("cost", cost);
        project.put("funded", true);
        project.put("hits", 0);
        project.put("message_count", 0);
        return jdbcInsertProject.executeAndReturnKey(project);
    }

    /**
     * Creates a project category association given a project id and a category id.
     * @param projectId Project id
     * @param categoryId Category id
     */
    private void createProjectCategory(Number projectId, Number categoryId) {
        Map<String, Long> values2 = new HashMap<>();
        values2.put("category_id", categoryId.longValue());
        values2.put("project_id", projectId.longValue());
        jdbcInsertProjectCategory.execute(values2);
    }

    /**
     * Creates filters for searching keyword projects
     * @param keyword keyword to look for
     * @param field field to search keyword in
     * @return The map with the criteria
     */
    private Map<String, Object> getKeywordMap(String keyword, String field) {
        Map<String, Object> map = new HashMap<>();
        map.put(field, keyword);
        return map;
    }

    /**
     * Creates filters for searching range projects
     * @param minCost minimun cost
     * @param maxCost minimun cost
     * @return The map with the criteria
     */
    private Map<String, Object> getRangeMap(String minCost, String maxCost) {
        Map<String, Object> map = new HashMap<>();
        map.put("minCost", minCost);
        map.put("maxCost", maxCost);
        return map;
    }

    /**
     * Creates filters for searching categories projects
     * @param categoryId Category id.
     * @return The map with the criteria
     */
    private Map<String, Object> getCategoryMap(Number categoryId) {
        Map<String, Object> map = new HashMap<>();
        map.put("category", categoryId.longValue());
        return map;
    }

    /**
     * Creates filters for searching owner's projects
     * @param userId Owner user id.
     * @return The map with the criteria
     */
    private Map<String, Object> getOwnerMap(Number userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("owner", new User(userId.longValue()));
        map.put("funded", true);
        return map;
    }

    /**
     * Creates filters for searching projects
     * @param filters Map of filters to apply
     * @return The list with the criteria
     */
    private List<FilterCriteria> getFilterList(Map<String, Object> filters) {
        filters.values().removeIf(value -> (value == null || value.toString().equals("")));
        List<FilterCriteria> filterList = new ArrayList<>();
        filters.forEach((key, value) -> filterList.add(new FilterCriteria(key, value)));
        return filterList;
    }
}
