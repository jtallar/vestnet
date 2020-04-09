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
            new Category(rs.getInt("id"), rs.getString("category"), null);


    @Autowired
    public CategoriesJdbcDao (final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
    @Override
    public List<Category> findAllCats() {
        List<Category> catList = jdbcTemplate.query("SELECT * FROM categories", ROW_MAPPER);
        return catList;
    }
}
