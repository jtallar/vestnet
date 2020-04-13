package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Category;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class CategoriesJdbcDaoTest {
    private static final String CATEGORIES_TABLE = "categories";
    private static final String CATEGORY_NAME = "Technology";
    private static final String PROJECTS_TABLE = "projects";
    private static final String PROJECT_CATEGORY_TABLE = "project_categories";

    @Autowired
    private DataSource ds;

    @Autowired
    private CategoriesJdbcDao categoriesJdbcDao;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsertCategory, jdbcInsertProject, jdbcInsertProjectCategory;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsertCategory = new SimpleJdbcInsert(ds)
                .withTableName(CATEGORIES_TABLE)
                .usingGeneratedKeyColumns("id");
        jdbcInsertProject = new SimpleJdbcInsert(ds)
                .withTableName(PROJECTS_TABLE)
                .usingGeneratedKeyColumns("id");
        jdbcInsertProjectCategory = new SimpleJdbcInsert(ds)
                .withTableName(PROJECT_CATEGORY_TABLE);

        JdbcTestUtils.deleteFromTables(jdbcTemplate, CATEGORIES_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, PROJECTS_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, PROJECT_CATEGORY_TABLE);
    }

    @Test
    public void testFindAllIfTableEmpty() {
        // 1: Precondiciones: Setup del escenario para que nuestro test corra
        // TABLE EMPTY

        // 2: Ejercitacion: Hacemos la unica llamada al metodo que queremos testear
        List<Category> categories = categoriesJdbcDao.findAllCats();

        // 3: Postcondiciones: Hacemos los pocos assertes que permiten validar correctitud
        assertTrue(categories.isEmpty());
    }

    @Test
    public void testFindAllIfTableNotEmpty() {
        // 1
        Map<String, String> values = new HashMap<String, String>();
        values.put("category", CATEGORY_NAME);
        jdbcInsertCategory.executeAndReturnKey(values);

        // 2
        List<Category> categories = categoriesJdbcDao.findAllCats();

        // 3
        assertEquals(1, categories.size());
        assertEquals(CATEGORY_NAME, categories.get(0).getName());
        assertEquals(0, categories.get(0).getParent());
    }

    @Test
    public void testFindProjectCategoriesWithNoProject() {
        // 1
        // TABLES EMPTY

        // 2
        List<Category> categories = categoriesJdbcDao.findProjectCategories(1);

        // 3
        assertTrue(categories.isEmpty());
    }

    @Test
    public void testFindProjectCategoriesWithProject() {
        // 1
        Map<String, String> category = new HashMap<String, String>();
        category.put("category", CATEGORY_NAME);
        Number categoryId = jdbcInsertCategory.executeAndReturnKey(category);

        Map<String, Object> project = new HashMap<>();
        project.put("project_name", "Jorge");
        project.put("summary", "SUMMRAy");
        Number projectId = jdbcInsertProject.executeAndReturnKey(project);

        Map<String, Long> values = new HashMap<String, Long>();
        values.put("category_id", categoryId.longValue());
        values.put("project_id", projectId.longValue());
        jdbcInsertProjectCategory.execute(values);

        // 2
        List<Category> categories = categoriesJdbcDao.findProjectCategories(projectId.longValue());

        // 3
        assertEquals(1, categories.size());
        assertEquals(CATEGORY_NAME, categories.get(0).getName());
        assertEquals(0, categories.get(0).getParent());
    }
}
