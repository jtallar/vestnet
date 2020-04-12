package ar.edu.itba.paw.persistence;


import ar.edu.itba.paw.interfaces.ProjectDao;
import ar.edu.itba.paw.model.Category;
import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.Stage;
import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ProjectJdbcDao implements ProjectDao {

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @Autowired
    private UserJdbcDao userJdbcDao;
    @Autowired
    private CategoriesJdbcDao categoriesJdbcDao;
    // TODO: VER SI SE PUEDE HACER DE OTRA MANERA
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final static RowMapper<Project> ROW_MAPPER = (rs, rowNum) -> new Project(rs.getLong("id"),
            rs.getString("project_name"), rs.getString("summary"),
            rs.getDate("publish_date"), rs.getDate("update_date"),
            rs.getLong("cost"), rs.getLong("hits"), rs.getBoolean("images"),
            rs.getLong("owner_id"),
            new Project.ProjectBackOffice(rs.getBoolean("aproved"), rs.getInt("profit_index"),
                    rs.getInt("risk_index")), null, null);

    @Autowired
    public ProjectJdbcDao(final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("projects")
                .usingGeneratedKeyColumns("id");

        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public Optional<Project> findById(long id) {
        Optional<Project> mayBeProject = jdbcTemplate.query("SELECT * FROM projects WHERE projects.id = ?", ROW_MAPPER, id)
                .stream().findFirst();
        if (mayBeProject.isPresent()) {
            // TODO:ADD STAGES
            Optional<User> owner = userJdbcDao.findById(mayBeProject.get().getOwnerUserId());
            if (owner.isPresent()) mayBeProject.get().setOwner(owner.get());
            mayBeProject.get().setCategories(categoriesJdbcDao.findProjectCategories(mayBeProject.get().getId()));
        }
        return mayBeProject;
    }

    @Override
    public List<Project> findAll() {
        // TODO: TEST WHAT HAPPENS IF NO PROJECTS ARE LOADED --> NULL o EMPTY LIST?
        List<Project> projects = jdbcTemplate.query("SELECT * FROM projects", ROW_MAPPER);
        for(Project project : projects) {
            // TODO: NO AGREGAR STAGES, NO?
            Optional<User> owner = userJdbcDao.findById(project.getOwnerUserId());
            if (owner.isPresent()) project.setOwner(owner.get());
            project.setCategories(categoriesJdbcDao.findProjectCategories(project.getId()));
        }
        return projects;
    }

    // TODO: VER SI HACE FALTA EL namedParamaeter o se puede de otra manera
    @Override
    public List<Project> findByCategories(List<Category> categories) {
        if (categories == null) return findAll();

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("categories", categories.stream().map(Category::getId).collect(Collectors.toList()));

//        List<Project> projects = jdbcTemplate.query("SELECT projects.id, projects.project_name, projects.summary," +
//                        "projects.publish_date, projects.update_date, projects.cost, projects.hits, projects.images, " +
//                        "projects.owner_id, projects.aproved, projects.profit_index, projects.risk_index " +
//                        "FROM projects JOIN project_categories ON projects.id = project_categories.project_id " +
//                        "WHERE project_categories.category_id IN (?)",
//                ROW_MAPPER, categories.stream().map(Category::getId).collect(Collectors.toList()));

        List<Project> projects = namedParameterJdbcTemplate.query("SELECT projects.id, projects.project_name, projects.summary," +
                "projects.publish_date, projects.update_date, projects.cost, projects.hits, projects.images, " +
                        "projects.owner_id, projects.aproved, projects.profit_index, projects.risk_index " +
                        "FROM projects JOIN project_categories ON projects.id = project_categories.project_id " +
                "WHERE project_categories.category_id IN (:categories)",
                parameters, ROW_MAPPER);
        for(Project project : projects) {
            // TODO:ADD STAGES?
            Optional<User> owner = userJdbcDao.findById(project.getOwnerUserId());
            if (owner.isPresent()) project.setOwner(owner.get());
            project.setCategories(categoriesJdbcDao.findProjectCategories(project.getId()));
        }
        return projects;
    }

    @Override
    public Project create(String name, String summary, Date publishDate, Date updateDate, long cost, User owner,
                          List<Category> categories, List<Stage> stages) {
        Map<String, Object> values = new HashMap<>();
        values.put("project_name", name);
        values.put("summary", summary);
        values.put("publish_date", publishDate);
        values.put("update_date", updateDate);
        values.put("cost", cost);
        values.put("owner_id", owner.getId());
        Number keyNumber = jdbcInsert.executeAndReturnKey(values);
        // TODO: INSERTAR LAS CATEGORIAS
        // TODO: INSERTAR LOS STAGES
        return new Project(keyNumber.longValue(), name, summary, publishDate, updateDate, cost, 0, false, owner,
                new Project.ProjectBackOffice(false, 0, 0), categories,
                stages.stream().map(Stage::getId).collect(Collectors.toList()), stages);
    }
}


