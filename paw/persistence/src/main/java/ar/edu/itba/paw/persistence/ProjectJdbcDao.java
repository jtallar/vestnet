package ar.edu.itba.paw.persistence;


import ar.edu.itba.paw.interfaces.ProjectDao;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.components.Pair;
import ar.edu.itba.paw.model.components.ProjectFilter;
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

import static ar.edu.itba.paw.persistence.JdbcProjectQueryBuilder.*;
import static ar.edu.itba.paw.persistence.JdbcQueries.*;

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
                .withTableName(PROJECT_TABLE)
                .usingGeneratedKeyColumns("id")
                .usingColumns("owner_id", "project_name", "summary", "cost", "images");
        jdbcInsertCategoryLink = new SimpleJdbcInsert(dataSource)
                .withTableName(PROJECT_CATEGORIES_TABLE);
        jdbcInsertFavorite = new SimpleJdbcInsert(dataSource)
                .withTableName(FAVORITES_TABLE);

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
        return jdbcTemplate.query(PROJECT_FIND_BY_ID, RESULT_SET_EXTRACTOR, projectId).stream().findFirst();
    }

    @Override
    public List<Project> findByIds(List<Long> ids) {
        if(ids == null || ids.isEmpty()) return new ArrayList<>();
        return namedParameterJdbcTemplate.query(PROJECT_FIND_BY_IDS, new MapSqlParameterSource().addValue("ids", ids), RESULT_SET_EXTRACTOR);
    }

    @Override
    public List<Project> findByOwner(long userId) {

        List<Project> projects = jdbcTemplate.query(PROJECT_FIND_BY_OWNER, new Object[] {userId}, RESULT_SET_EXTRACTOR);

        projects.forEach(project -> {
            if(project.getNotRead() == null) project.setNotRead(0);
        }); //set 0 if not read because it sets null


        return  projects;

    }

    @Override
    public List<Project> findFiltered(ProjectFilter filter) {
        Pair<String, MapSqlParameterSource> pair = buildQueryAndParams(filter, true);
        List<Integer> ids = namedParameterJdbcTemplate.queryForList(pair.getKey(), pair.getValue(), Integer.class);
        if (ids.isEmpty()) return new ArrayList<>();
        return namedParameterJdbcTemplate.query(selectProjects(filter.getSort().getId()), new MapSqlParameterSource().addValue("ids", ids), RESULT_SET_EXTRACTOR);
    }

    @Override
    public Integer countFiltered(ProjectFilter filter) {
        Pair<String, MapSqlParameterSource> pair = buildQueryAndParams(filter, false);
        return namedParameterJdbcTemplate.queryForObject(pair.getKey(), pair.getValue(), Integer.class);
    }

    @Override
    public byte[] findImageForProject(long projectId) {
        return jdbcTemplate.queryForObject(PROJECT_IMAGE, new Object[] {projectId}, byte[].class);
    }

    @Override
    public void addHit(long projectId) {
        jdbcTemplate.update(PROJECT_ADD_HIT, projectId);
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
        jdbcTemplate.update(USER_DELETE_FAVORITE, new Object[]{projectId,userId});
    }

    @Override
    public boolean isFavorite(long projectId, long userId) {
        return findFavorites(userId).contains(projectId);
    }

    @Override
    public List<Long> findFavorites(long user_id) {
        return jdbcTemplate.query(USER_FIND_FAVORITES, new Object[] {user_id}, RESULT_SET_EXTRACTOR_PID);
    }

    @Override
    public long getFavoritesCount(long projectId) {
        return jdbcTemplate.queryForObject(PROJECT_COUNT_FAVORITE, new Object[] {projectId}, Long.class);
    }

    @Override
    public List<Long> getFavoritesCount(List<Long> projectIds) {
        if (projectIds == null || projectIds.size() == 0) return new ArrayList<>();
        String inSql = String.join("),(", Collections.nCopies(projectIds.size(), "?"));
        return jdbcTemplate.queryForList(String.format(PROJECTS_FAVORITE_COUNT, inSql), projectIds.toArray(), long.class);
    }

    @Override
    public List<Boolean> isFavorite(List<Long> projectIds, long userId) {
        if (projectIds == null || projectIds.size() == 0) return new ArrayList<>();
        String inSql = String.join("),(", Collections.nCopies(projectIds.size(), "?"));
        projectIds.add(userId);
        return jdbcTemplate.queryForList(String.format(PROJECT_IS_FAVORITE_BY_ID_USER, inSql), projectIds.toArray(), boolean.class);
    }


    /**
     * Auxiliary functions
     */

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

    /**
     * Creates the dynamic query to mbe executed and the parameter with it.
     * @param filter The filters to be applied.
     * @param getIds If true gets id list, else gets count.
     * @return Pair with the formatted query, and the query params.
     */
    private Pair<String, MapSqlParameterSource> buildQueryAndParams(ProjectFilter filter, boolean getIds) {
        JdbcProjectQueryBuilder queryBuilder = new JdbcProjectQueryBuilder();
        MapSqlParameterSource parameters = new MapSqlParameterSource();

        if (getIds) queryBuilder.selectProjectIds();
        else queryBuilder.selectProjectCount();

        if (filter.isMinCost()) {
            parameters.addValue("minCost", filter.getMinCost());
            queryBuilder.addMinCost();
        }

        if (filter.isMaxCost()) {
            parameters.addValue("maxCost", filter.getMaxCost());
            queryBuilder.addMaxCost();
        }

        if (filter.isCategory()) {
            parameters.addValue("category", filter.getCategory());
            queryBuilder.addCategory();
        }

        if (filter.isSearch()) {
            parameters.addValue("keyword", "%" + filter.getKeyword().toLowerCase() + "%");
            queryBuilder.addSearch(SearchQuery.getEnum(filter.getSearchField().getId()));
        }

        // If ask for count query, limit, offset, and sort are not needed.
        if (!getIds) return new Pair<>(queryBuilder.getQuery(), parameters);

        parameters .addValue("limit", filter.getLimit());
        parameters.addValue("offset", (filter.getPage() - 1) * filter.getLimit());

        queryBuilder.addSort(SortQuery.getEnum(filter.getSort().getId()));
        queryBuilder.addLimitOffset();

        return new Pair<>(queryBuilder.getQuery(), parameters);
    }
}


