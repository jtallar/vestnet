package ar.edu.itba.paw.persistence;


import ar.edu.itba.paw.interfaces.ProjectDao;
import ar.edu.itba.paw.model.Category;
import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
/*
@Repository

public class ProjectJdbcDao implements ProjectDao {



    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    private final static RowMapper<Project> ROW_MAPPER = (rs, rowNum) -> new Project(rs.getInt("id"), rs.getString("project_name",rs.getString("summary"),
            rs.getDate("publish_date"),rs.getDate("update_date"),rs.getInt("cost"),rs.getInt("hits"), rs.getBoolean("images"),
            (User)rs.getObject("owner_id"),
            (Project.ProjectBackOffice) new Project.ProjectBackOffice (rs.getBoolean("aproved"), rs.getInt("profit_index"), rs.getInt("risk_index"),
                    (List<Category>) rs.getObject("category"),(List))
            )
    @Autowired
    public ProjectJdbcDao(final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("startUp")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Project> findAllProjects() {
        // just testing before database connection

        List<Project> listPro = new LinkedList<>();
        Date date = new Date();
        Project samsung = new Project(1, "Samsung", "Una empresa que vende electrodomésticos",1,new Date());
        Project lg = new Project(2, "LG", "Otra empresa que vende electrodomésticos",1,new Date());
        Project google = new Project(3, "Google", "StartUp de informática",2,new Date());

        listPro.add(samsung);
        listPro.add(lg);
        listPro.add(google);

        return listPro;
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


 */
