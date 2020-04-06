package ar.edu.itba.paw.persistence;


import ar.edu.itba.paw.interfaces.ProjectDao;
import ar.edu.itba.paw.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

@Repository

public class ProjectJdbcDao implements ProjectDao {

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    private final static RowMapper<Project> ROW_MAPPER = (rs, rowNum) -> new Project(rs.getInt("id"), rs.getString("name"), rs.getString("summary"), rs.getInt("ownerId"), rs.getDate("date"));

    @Autowired
    public ProjectJdbcDao(final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("startUp")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Optional<Project> findById(long id) {
        return jdbcTemplate.query("SELECT * FROM startUp WHERE id = ?", new Object[] {id}, ROW_MAPPER)
                .stream().findFirst();
    }

    @Override
    public List<Project> findByName(String name) {
        return jdbcTemplate.query("SELECT * FROM startUp WHERE name = ?", ROW_MAPPER, name);
    }

    @Override
    public Project create(String name, String summary, long ownerId, Date date) {
        Map<String, Object> values = new HashMap<String, Object>();
        values.put("name", name);
        values.put("summary", summary);
        values.put("ownerId", ownerId);
        values.put("date", date);
        Number keyNumber = jdbcInsert.executeAndReturnKey(values);

        return new Project(keyNumber.longValue(), name, summary, ownerId, date);
    }

    //we will want to add list of projects
}
