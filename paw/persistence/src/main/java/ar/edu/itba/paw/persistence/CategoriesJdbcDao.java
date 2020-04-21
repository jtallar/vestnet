package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.CategoriesDao;
import ar.edu.itba.paw.model.Category;
import org.simpleflatmapper.jdbc.spring.JdbcTemplateMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;


@Repository
public class CategoriesJdbcDao implements CategoriesDao {
    private JdbcTemplate jdbcTemplate;

    private final static ResultSetExtractor<List<Category>> RESULT_SET_EXTRACTOR = JdbcTemplateMapperFactory
            .newInstance()
            .addKeys("id")
            .newResultSetExtractor(Category.class);

    @Autowired
    public CategoriesJdbcDao (final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * Lists all the available categories.
     * @return List of all available categories.
     */
    @Override
    public List<Category> findAll() {
        return jdbcTemplate.query(JdbcQueries.CATEGORY_FIND_ALL, RESULT_SET_EXTRACTOR);
    }

    /**
     * Finds all the categories of a project given its id.
     * @param projectId The unique project id.
     * @return A list with all the project categories.
     */
    @Override
    public List<Category> findProjectCategories(long projectId) { // TODO check if its needed
        return jdbcTemplate.query(JdbcQueries.CATEGORY_FIND_BY_PROJECT_ID, RESULT_SET_EXTRACTOR, projectId);
    }
}
