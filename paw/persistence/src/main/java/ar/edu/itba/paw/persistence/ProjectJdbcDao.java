package ar.edu.itba.paw.persistence;


import ar.edu.itba.paw.interfaces.ProjectDao;
import ar.edu.itba.paw.model.*;
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
    private SimpleJdbcInsert jdbcInsert, jdbcInsertCategoryLink, jdbcInsertFavorite;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

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

    @Override
    public long create(String name, String summary, long cost, long ownerId, List<Long> categoriesIds, List<Stage> stages, byte[] imageBytes) {
        Map<String, Object> values = new HashMap<>();
        values.put("owner_id", ownerId);
        values.put("project_name", name);
        values.put("summary", summary);
        values.put("cost", cost);
        if (imageBytes.length != 0) values.put("images", imageBytes);

        long finalId = jdbcInsert.executeAndReturnKey(values).longValue();
        return insertCategories(categoriesIds, finalId);
    }

    @Override
    public Optional<Project> findById(long projectId) {
        return jdbcTemplate.query(JdbcQueries.PROJECT_FIND_BY_ID, RESULT_SET_EXTRACTOR, projectId).stream().findFirst();
    }

    @Override
    public List<Project> findByOwner(long userId) {
        return jdbcTemplate.query(JdbcQueries.PROJECT_FIND_BY_OWNER, new Object[] {userId}, RESULT_SET_EXTRACTOR);
    }

    @Override
    public Integer countByCost(long minCost, long maxCost) {
        return jdbcTemplate.queryForObject(JdbcQueries.PROJECT_COUNT_COST_MATCH,new Object[] {minCost, maxCost}, Integer.class);
    }

    @Override
    public List<Project> findByCoincidencePage(String name, String selection, int pageStart, int pageOffset) {
        List<Integer> ids = findIdsByCoincidencePaged("%" + name + "%", selection, pageStart, pageOffset);

        MapSqlParameterSource parAux = new MapSqlParameterSource("ids", ids);
        return namedParameterJdbcTemplate.query(JdbcQueries.PROJECT_FIND_BY_IDS, parAux, RESULT_SET_EXTRACTOR);
    }

    @Override
    public Integer countByCoincidence(String name, String selection) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", "%" + name + "%");

        switch (selection) {
            case "all": return namedParameterJdbcTemplate.queryForObject(JdbcQueries.PROJECT_COUNT_ALL_NAME_MATCH, parameters, Integer.class);
            case "project_info": return namedParameterJdbcTemplate.queryForObject(JdbcQueries.PROJECT_COUNT_NAME_SUMMARY_MATCH, parameters, Integer.class);
            case "owner_name": return namedParameterJdbcTemplate.queryForObject(JdbcQueries.PROJECT_COUNT_FULL_NAME_MATCH, parameters, Integer.class);
            case "owner_email": return namedParameterJdbcTemplate.queryForObject(JdbcQueries.PROJECT_COUNT_EMAIL_MATCH, parameters, Integer.class);
            case "loc": return namedParameterJdbcTemplate.queryForObject(JdbcQueries.PROJECT_COUNT_LOCATION_MATCH, parameters, Integer.class);
            default: return 0;
        }
    }

    @Override
    public List<Project> findByCategoryPage(List<Category> categories, int pageStart, int pageOffset, long minCost, long maxCost) {
        if(pageOffset == 0 || categories == null) return new ArrayList<>();

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("from", pageStart)
                .addValue("to", pageOffset)
                .addValue("categories", categories.stream().map(Category::getId).collect(Collectors.toList()))
                .addValue("min", minCost)
                .addValue("max", maxCost);

        List<Integer> ids = namedParameterJdbcTemplate.queryForList(JdbcQueries.PROJECT_IDS_CATEGORY_COST_MATCH, parameters, Integer.class);

        MapSqlParameterSource id_par = new MapSqlParameterSource().addValue("ids", ids);
        return namedParameterJdbcTemplate.query(JdbcQueries.PROJECT_FIND_BY_IDS, id_par, RESULT_SET_EXTRACTOR);
    }

    @Override
    public Integer countByCategory(List<Category> categories, long minCost, long maxCost) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("categories", categories.stream().map(Category::getId).collect(Collectors.toList()))
                .addValue("min", minCost)
                .addValue("max", maxCost);

        return namedParameterJdbcTemplate.queryForObject(JdbcQueries.PROJECT_COUNT_CATEGORY_COST_MATCH, parameters, Integer.class);
    }

    @Override
    public List<Project> findByCostPage(int pageStart, int pageOffset, long minCost, long maxCost) {
        if(pageOffset == 0) return new ArrayList<>();

        List<Integer> ids = jdbcTemplate.queryForList(JdbcQueries.PROJECT_IDS_COST_MATCH, new Object[]{minCost, maxCost, pageStart, pageOffset}, Integer.class);

        MapSqlParameterSource parameters = new MapSqlParameterSource().addValue("ids", ids);
        return namedParameterJdbcTemplate.query(JdbcQueries.PROJECT_FIND_BY_IDS, parameters, RESULT_SET_EXTRACTOR);
    }

    @Override
    public byte[] findImageForProject(long projectId) {
        return jdbcTemplate.queryForObject(JdbcQueries.PROJECT_IMAGE, new Object[] {projectId}, byte[].class);
    }

    @Override
    public void addHit(long projectId) {
        jdbcTemplate.update(JdbcQueries.PROJECT_ADD_HIT, projectId);
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
        jdbcTemplate.update(JdbcQueries.USER_DELETE_FAVORITE, new Object[]{projectId,userId});
    }

    @Override
    public boolean isFavorite(long projectId, long userId) {
        return findFavorites(userId).contains(projectId);
    }

    @Override
    public List<Long> findFavorites(long user_id) {
        return jdbcTemplate.query(JdbcQueries.USER_FIND_FAVORITES, new Object[] {user_id}, RESULT_SET_EXTRACTOR_PID);
    }

    @Override
    public long getFavoritesCount(long projectId) {
        return jdbcTemplate.queryForObject(JdbcQueries.PROJECT_COUNT_FAVORITE, new Object[] {projectId}, Long.class);
    }

    @Override
    public List<Boolean> isFavorite(List<Long> projectIds, long userId) {
        if (projectIds == null || projectIds.size() == 0) return new ArrayList<>();
        String inSql = String.join("),(", Collections.nCopies(projectIds.size(), "?"));
        projectIds.add(userId);
        return jdbcTemplate.queryForList(String.format(JdbcQueries.PROJECT_IS_FAVORITE_BY_ID_USER, inSql), projectIds.toArray(), boolean.class);
    }


    /* Auxiliary functions */

    /**
     * Inserts all the categories for a given project.
     * @param categoriesIds List of all categories id.
     * @param projectId The unique project id.
     * @return The project id.
     */
    private long insertCategories(List<Long> categoriesIds, long projectId) {
        Map<String, Object> values = new HashMap<>();
        for (long categoryId : categoriesIds) {
            values.put("project_id", projectId);
            values.put("category_id", categoryId);
            jdbcInsertCategoryLink.execute(values);
        }
        return projectId;
    }

    private List<Integer> findIdsByCoincidencePaged(String name, String selection, int pageStart, int pageOffset) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", name)
                .addValue("from", pageStart)
                .addValue("to", pageOffset);

        switch (selection) {
            case "all": return namedParameterJdbcTemplate.queryForList(JdbcQueries.PROJECT_IDS_ALL_NAME_MATCH, parameters, Integer.class);
            case "project_info": return namedParameterJdbcTemplate.queryForList(JdbcQueries.PROJECT_IDS_NAME_SUMMARY_MATCH, parameters, Integer.class);
            case "owner_name": return namedParameterJdbcTemplate.queryForList(JdbcQueries.PROJECT_IDS_FULL_NAME_MATCH, parameters, Integer.class);
            case "owner_email": return namedParameterJdbcTemplate.queryForList(JdbcQueries.PROJECT_IDS_EMAIL_MATCH, parameters, Integer.class);
            case "loc": return namedParameterJdbcTemplate.queryForList(JdbcQueries.PROJECT_IDS_LOCATION_MATCH, parameters, Integer.class);
            default: return new ArrayList<>();
        }
    }
}


