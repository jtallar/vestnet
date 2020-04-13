package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Category;
import ar.edu.itba.paw.model.Entrepreneur;
import ar.edu.itba.paw.model.Project;
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
    private static final String PROJECT_NAME = "Project", PROJECT_SUMMARY = "Summary";

    private static final String COUNTRIES_TABLE = "countries";
    private static final String STATES_TABLE = "states";
    private static final String CITIES_TABLE = "cities";
    private static final String ROLES_TABLE = "roles";
    private static final String USERS_TABLE = "users";

    private static final String CATEGORIES_TABLE = "categories";
    private static final String CATEGORY_NAME = "Technology";
    private static final String PROJECT_CATEGORY_TABLE = "project_categories";

    @Autowired
    private DataSource ds;

    @Autowired
    private ProjectJdbcDao projectJdbcDao;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsertProject, jdbcInsertUser, jdbcInsertCategory, jdbcInsertProjectCategory;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsertProject = new SimpleJdbcInsert(ds)
                .withTableName(PROJECTS_TABLE)
                .usingGeneratedKeyColumns("id");
        jdbcInsertUser = new SimpleJdbcInsert(ds)
                .withTableName(USERS_TABLE)
                .usingGeneratedKeyColumns("id");
        jdbcInsertCategory = new SimpleJdbcInsert(ds)
                .withTableName(CATEGORIES_TABLE)
                .usingGeneratedKeyColumns("id");
        jdbcInsertProjectCategory = new SimpleJdbcInsert(ds)
                .withTableName(PROJECT_CATEGORY_TABLE);

        JdbcTestUtils.deleteFromTables(jdbcTemplate, USERS_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, PROJECTS_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, CITIES_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, STATES_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, COUNTRIES_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, ROLES_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, CATEGORIES_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, PROJECT_CATEGORY_TABLE);
    }

    @Test
    public void testCreate() {
        // 1
        Map<String, Object> user = new HashMap<>();
        user.put("first_name", "Jorge");
        user.put("last_name", "SUMMRAy");
        user.put("real_id", "SUMMRAy");
        user.put("email", "SUMMRAy@dsada.com");
        user.put("aux_date", new Date());
        Number userId = jdbcInsertUser.executeAndReturnKey(user);

        // 2
        Project project = projectJdbcDao.create(PROJECT_NAME, PROJECT_SUMMARY, null, null, 0,
                new Entrepreneur(userId.longValue(), null, null, null, null, null, null, null, null, null, null, 0),
                new ArrayList<>(), new ArrayList<>());

        // 3
        System.out.println(project.getId());
        assertEquals(PROJECT_NAME, project.getName());
        assertEquals(PROJECT_SUMMARY, project.getSummary());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, PROJECTS_TABLE));
    }

    @Test
    public void testFindByIdDoesntExists() {
        // 1
        // TABLE EMPTY

        // 2
        Optional<Project> maybeProject = projectJdbcDao.findById(1);

        // 3
        assertFalse(maybeProject.isPresent());
    }

    @Test
    public void testFindByIdProjectExists() {
        // 1
        Map<String, Object> project = new HashMap<>();
        project.put("project_name", PROJECT_NAME);
        project.put("summary", PROJECT_SUMMARY);
        Number projectId = jdbcInsertProject.executeAndReturnKey(project);

        // 2
        Optional<Project> maybeProject = projectJdbcDao.findById(projectId.longValue());

        // 3
        assertTrue(maybeProject.isPresent());
        assertEquals(PROJECT_NAME, maybeProject.get().getName());
        assertEquals(PROJECT_SUMMARY, maybeProject.get().getSummary());
    }

    @Test
    public void testFindAllIfTableEmpty() {
        // 1
        // TABLE EMPTY

        // 2
        List<Project> projects = projectJdbcDao.findAll();

        // 3
        assertTrue(projects.isEmpty());
    }

    @Test
    public void testFindAllIfTableNotEmpty() {
        // 1
        Map<String, Object> values = new HashMap<>();
        values.put("project_name", PROJECT_NAME);
        values.put("summary", PROJECT_SUMMARY);
        jdbcInsertProject.executeAndReturnKey(values);

        // 2
        List<Project> projects = projectJdbcDao.findAll();

        // 3
        assertEquals(1, projects.size());
        assertEquals(PROJECT_NAME, projects.get(0).getName());
        assertEquals(PROJECT_SUMMARY, projects.get(0).getSummary());
    }

    @Test
    public void testFindByCategoriesNull() {
        // 1
        Map<String, Object> values = new HashMap<>();
        values.put("project_name", PROJECT_NAME);
        values.put("summary", PROJECT_SUMMARY);
        jdbcInsertProject.executeAndReturnKey(values);

        // 2
        List<Project> projects = projectJdbcDao.findByCategories(null);

        // 3
        assertEquals(1, projects.size());
        assertEquals(PROJECT_NAME, projects.get(0).getName());
        assertEquals(PROJECT_SUMMARY, projects.get(0).getSummary());
    }

    @Test
    public void testFindByCategoriesEmpty() {
        // 1
        Map<String, Object> values = new HashMap<>();
        values.put("project_name", PROJECT_NAME);
        values.put("summary", PROJECT_SUMMARY);
        jdbcInsertProject.executeAndReturnKey(values);

        // 2
        List<Project> projects = projectJdbcDao.findByCategories(new ArrayList<>());

        // 3
        assertEquals(1, projects.size());
        assertEquals(PROJECT_NAME, projects.get(0).getName());
        assertEquals(PROJECT_SUMMARY, projects.get(0).getSummary());
    }

    @Test
    public void testFindByCategoriesValid() {
        // 1
        Map<String, String> category = new HashMap<String, String>();
        category.put("category", CATEGORY_NAME);
        Number categoryId = jdbcInsertCategory.executeAndReturnKey(category);

        Map<String, Object> project = new HashMap<>();
        project.put("project_name", PROJECT_NAME);
        project.put("summary", PROJECT_SUMMARY);
        Number projectId = jdbcInsertProject.executeAndReturnKey(project);

        Map<String, Long> values = new HashMap<String, Long>();
        values.put("category_id", categoryId.longValue());
        values.put("project_id", projectId.longValue());
        jdbcInsertProjectCategory.execute(values);

        // 2
        List<Project> projects = projectJdbcDao.findByCategories(Collections.singletonList
                (new Category(categoryId.longValue(), CATEGORY_NAME, 0)));

        // 3
        assertEquals(1, projects.size());
        assertEquals(PROJECT_NAME, projects.get(0).getName());
        assertEquals(PROJECT_SUMMARY, projects.get(0).getSummary());
    }
}
