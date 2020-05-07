package ar.edu.itba.paw.persistence;


import ar.edu.itba.paw.interfaces.ProjectDao;
import ar.edu.itba.paw.model.*;
import org.simpleflatmapper.jdbc.spring.JdbcTemplateMapperFactory;
import org.simpleflatmapper.jdbc.spring.ResultSetExtractorImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ProjectJdbcDao implements ProjectDao {
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert, jdbcInsertCategoryLink, jdbcInsertFavorite;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    private UserJdbcDao userJdbcDao;
    private CategoriesJdbcDao categoriesJdbcDao;



    private final static ResultSetExtractor<List<Project>> RESULT_SET_EXTRACTOR = JdbcTemplateMapperFactory
            .newInstance()
            .addKeys("id")
            .newResultSetExtractor(Project.class);

    private final static ResultSetExtractor<List<Long>> RESULT_SET_EXTRACTOR_PID = JdbcTemplateMapperFactory
            .newInstance()
            .addKeys("id")
            .newResultSetExtractor(Long.class);

    @Autowired
    public ProjectJdbcDao(final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);

        // NOTE: Use columns when adding fields in create otherwise defaults wont work
        jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(JdbcQueries.PROJECT_TABLE)
                .usingGeneratedKeyColumns("id")
                .usingColumns("owner_id", "project_name", "summary", "cost", "images");
        jdbcInsertCategoryLink = new SimpleJdbcInsert(dataSource)
                .withTableName(JdbcQueries.PROJECT_CATEGORIES_TABLE);
        jdbcInsertFavorite = new SimpleJdbcInsert(dataSource)
                .withTableName(JdbcQueries.FAVORITES_TABLE);

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

    @Override
    public Integer projectsCount(long min, long max) {
        Integer count = jdbcTemplate.queryForObject(JdbcQueries.COUNT_PROJECTS,new Object[] {min, max}, Integer.class);
        return count;
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

    @Override
    public List<Project> findCatForPage(List<Category> categories, int from, int to, long min, long max) {
        List<Project> projects = new ArrayList<>();
        if(to != 0){
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("from", from)
                .addValue("to", to)
                .addValue("categories", categories.stream().map(Category::getId).collect(Collectors.toList()))
                .addValue("min", min)
                .addValue("max", max);

        List<Integer> ids = namedParameterJdbcTemplate.queryForList(JdbcQueries.PROJECT_ID_FROM_PAGE_CATEGORY, parameters, Integer.class);
            MapSqlParameterSource id_par = new MapSqlParameterSource().addValue("ids", ids);
        projects = namedParameterJdbcTemplate.query(JdbcQueries.PROJECT_FIND_WITH_ID_LIST, id_par, RESULT_SET_EXTRACTOR);
        }
        return projects;
    }

    @Override
    public Integer searchProjCount(String name) {
        String search = "%" + name + "%";
        MapSqlParameterSource parameters = new MapSqlParameterSource().addValue("name", name);
        Integer count = namedParameterJdbcTemplate.queryForObject(JdbcQueries.SEARCH_PROJ_COUNT, parameters, Integer.class);

        return count;

    }

    @Override
    public Integer catProjCount(List<Category> categories, long min, long max) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("categories", categories.stream().map(Category::getId).collect(Collectors.toList()))
                .addValue("min", min)
                .addValue("max", max);

        Integer count = namedParameterJdbcTemplate.queryForObject(JdbcQueries.PROJECT_COUNT_CAT, parameters, Integer.class);
        // TODO add stages?
        return count;
    }

    // TODO: VER SI HACE FALTA DEVOLVER UN PROJECT O PUEDO DEVOLVER EL ID, TOTAL DE ACA DESEMBOCO EN BUSCARLO, NO?
    @Override
    public long create(String name, String summary, long cost, long ownerId, List<Long> categoriesIds, List<Stage> stages, byte[] imageBytes) {
        Map<String, Object> values = new HashMap<>();
        values.put("owner_id", ownerId);
        values.put("project_name", name);
        values.put("summary", summary);
        // TODO: SACAR ESTE COST, LUEGO CALCULARLO CON LOS STAGES
        values.put("cost", cost);
        // DATE SET TO DEFAULT --> CURRENT_TIMESTAMP
        // TODO: AGREGAR IMAGENES de slideshow
        if (imageBytes.length != 0) {
            values.put("images", imageBytes);
        }
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

    @Override
    public List<Project> findByOwner(long userId) {

        List<Project> projects = jdbcTemplate.query(JdbcQueries.PROJECT_FIND_BY_OWNER, new Object[] {userId}, RESULT_SET_EXTRACTOR);
        for(Project project : projects) {
            // TODO: NO AGREGAR STAGES, NO?
            //Optional<User> owner = userJdbcDao.findById(userId);
            //if (owner.isPresent()) project.setOwner(owner.get());
            //project.setCategories(categoriesJdbcDao.findProjectCategories(project.getId()));
        }
        return projects;
    }

    @Override
    public List<Project> findCoincidence(String name, int from, int to) {

        String search = "%" + name + "%";
        MapSqlParameterSource parameters = new MapSqlParameterSource().addValue("name", search)
                                             .addValue("from", from).addValue("to", to);

        List<Integer> ids =  namedParameterJdbcTemplate.queryForList(JdbcQueries.PROJECT_FIND_COINCIDENCE_ID, parameters, Integer.class);

        if(ids.isEmpty()){
            List<Project> projects = new ArrayList<>();
            return projects;
        }
        else {
            MapSqlParameterSource parAux = new MapSqlParameterSource("ids", ids);

            List<Project> projects = namedParameterJdbcTemplate.query(JdbcQueries.PROJECT_FIND_WITH_ID_LIST, parAux, RESULT_SET_EXTRACTOR);


            return projects;
        }
    }



    @Override
    public byte[] findImageForProject(long projectId) {
        return jdbcTemplate.queryForObject(JdbcQueries.PROJECT_IMAGE, new Object[] {projectId}, byte[].class);
    }

    @Override
    public List<Long> findFavorites(long user_id) {
        return jdbcTemplate.query(JdbcQueries.FAVORITES_PROJ, new Object[] {user_id}, RESULT_SET_EXTRACTOR_PID);
    }

    @Override
    public void addFavorite(long projectId, long userId) {
        Map<String, Object> values = new HashMap<>();
        values.put("project_id", projectId);
        values.put("user_id", userId);
        jdbcInsertFavorite.execute(values);
    }

    @Override
    public void deleteFavorite(long projectId, long userId) {
        Object[] args = new Object[]{projectId,userId};
        jdbcTemplate.update(JdbcQueries.DELETE_FAV, args);
    }

    @Override
    public boolean isFavorite(long projectId, long userId) {
        return findFavorites(userId).contains(projectId);
    }

    @Override
    public List<Project> findPage(int from, int to, long min, long max) {
        List<Project> projects;
        if(to != 0) {
            List<Integer> ids = jdbcTemplate.queryForList(JdbcQueries.PROJECT_ID_FROM_PAGE, new Object[]{min, max, from, to}, Integer.class);
            MapSqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("ids", ids);
            projects = namedParameterJdbcTemplate.query(JdbcQueries.PROJECT_FIND_WITH_ID_LIST, parameters, RESULT_SET_EXTRACTOR);
        }
        else {
            projects = new ArrayList<>(); 
        }
        return projects;
    }

}


