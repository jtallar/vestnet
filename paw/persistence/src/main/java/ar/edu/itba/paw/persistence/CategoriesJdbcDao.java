package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.CategoriesDao;
import ar.edu.itba.paw.model.Category;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Repository
public class CategoriesJdbcDao implements CategoriesDao {
    private JdbcTemplate jdbcTemplate;

    private final static RowMapper<Category> ROW_MAPPER = (rs, rowNum) ->
            new Category(rs.getInt("id"), rs.getString("category"), null);

    @Override
    public List<Category> findAllCats() {
        List<Category> catList = new ArrayList<>();

        catList = jdbcTemplate.queryForList("SELECT * FROM categories", Category.class);

        //List<Map<String, Object>> list = jdbcTemplate.queryForList("SELECT * FROM categories");


        return catList;
    }
}
