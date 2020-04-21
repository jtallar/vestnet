package ar.edu.itba.paw.persistence;


import ar.edu.itba.paw.interfaces.ProjectDao;
import ar.edu.itba.paw.model.Category;
import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.Stage;
import org.simpleflatmapper.jdbc.spring.JdbcTemplateMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ProjectJdbcDao implements ProjectDao {
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert, jdbcInsertCategoryLink;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final static ResultSetExtractor<List<Project>> RESULT_SET_EXTRACTOR = JdbcTemplateMapperFactory
            .newInstance()
            .addKeys("id")
            .newResultSetExtractor(Project.class);

    @Autowired
    public ProjectJdbcDao(final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);

        // NOTE: Use columns when adding fields in create otherwise defaults wont work
        jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(JdbcQueries.PROJECT_TABLE)
                .usingGeneratedKeyColumns("id")
                .usingColumns("owner_id", "project_name", "summary", "cost");
        jdbcInsertCategoryLink = new SimpleJdbcInsert(dataSource)
                .withTableName(JdbcQueries.PROJECT_CATEGORIES_TABLE);

        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    /**
     * Search for a specific project given its id.
     * @param id The unique id for the project
     * @return The entire project if found, otherwise null.
     */
    @Override
    public Optional<Project> findById(long id) {
        Optional<Project> project = jdbcTemplate.query(JdbcQueries.PROJECT_FIND_BY_ID, RESULT_SET_EXTRACTOR, id).stream().findFirst();
        // TODO add stages
        return project;
    }

    /**
     * Search for every available project in the DB
     * @return  A list of every single project with its owner and categories set (NO STAGES).
     *          If no projects are found, returns empty list
     */
    @Override
    public List<Project> findAll() {
        List<Project> projects = jdbcTemplate.query(JdbcQueries.PROJECT_FIND_ALL, RESULT_SET_EXTRACTOR);
        // TODO add stages?
        return projects;
    }

    /**
     * Search for every available project in the DB that matches the category list received
     * @param categories The list of categories to find
     * @return  A list of every single project with its owner and categories set (NO STAGES) that match those categories
     *          If no projects are found, returns empty list. If list is null or empty, returns all projects.
     */
    @Override
    public List<Project> findByCategories(List<Category> categories) {
        if (categories == null || categories.isEmpty()) return findAll();

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("categories", categories.stream().map(Category::getId).collect(Collectors.toList()));

        List<Project> projects = namedParameterJdbcTemplate.query(JdbcQueries.PROJECT_FIND_BY_CAT, parameters, RESULT_SET_EXTRACTOR);
        // TODO add stages?
        return projects;
    }


    // TODO: VER SI HACE FALTA DEVOLVER UN PROJECT O PUEDO DEVOLVER EL ID, TOTAL DE ACA DESEMBOCO EN BUSCARLO, NO?
    @Override
    public long create(String name, String summary, long cost, long ownerId, List<Long> categoriesIds, List<Stage> stages) {
        Map<String, Object> values = new HashMap<>();
        values.put("owner_id", ownerId);
        values.put("project_name", name);
        values.put("summary", summary);
        // TODO: SACAR ESTE COST, LUEGO CALCULARLO CON LOS STAGES
        values.put("cost", cost);
        // DATE SET TO DEFAULT --> CURRENT_TIMESTAMP
        // TODO: AGREGAR IMAGENES
        // IMAGES SET TO DEFAULT --> false
        // HITS SET TO DEFAULT --> 0
        // TODO: AGREGAR BACKOFFICE
        // BACKOFFICE SET TO DEFAULT

        Number keyNumber = jdbcInsert.executeAndReturnKey(values);
        for (long categoryId : categoriesIds) {
            values.clear();
            values.put("project_id", keyNumber.longValue());
            values.put("category_id", categoryId);
            jdbcInsertCategoryLink.execute(values);
        }
        // TODO: INSERTAR LOS STAGES con sus RESOURCES

        return keyNumber.longValue();
//        return new Project(keyNumber.longValue(), name, summary, publishDate, updateDate, cost, 0, false, owner,
//                new Project.ProjectBackOffice(false, 0, 0), categories,
//                stages.stream().map(Stage::getId).collect(Collectors.toList()), stages);
    }
}


