package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.CategoriesDao;
import ar.edu.itba.paw.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Repository
public class CategoriesJdbcDao implements CategoriesDao {
    private JdbcTemplate jdbcTemplate;

    private final static RowMapper<Category> ROW_MAPPER = (rs, rowNum) ->
            new Category(rs.getLong("id"), rs.getString("category"), rs.getLong("parent"));


    @Autowired
    public CategoriesJdbcDao (final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
    @Override
    public List<Category> findAllCats() {
        return jdbcTemplate.query("SELECT * FROM categories", ROW_MAPPER);
    }

    // TODO> ESTE SE SUPONE QUE VA ACA O EN PROJECT? LO NECESITA PROJECT, PERO NECESITO ESTE ROW MAPPER
    @Override
    public List<Category> findProjectCategories(long projectId) {
        return jdbcTemplate.query("SELECT categories.id, categories.category, categories.parent " +
                "FROM categories JOIN project_categories ON project_categories.category_id = categories.id " +
                "WHERE project_categories.project_id = ?", ROW_MAPPER, projectId);
    }
}
