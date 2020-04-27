package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.model.Location;
import ar.edu.itba.paw.model.User;
import org.simpleflatmapper.jdbc.spring.JdbcTemplateMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;


@Repository
public class UserJdbcDao implements UserDao {
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;
    private SimpleJdbcInsert passJdbcInsert;

    private final static ResultSetExtractor<List<User>> RESULT_SET_EXTRACTOR = JdbcTemplateMapperFactory
            .newInstance()
            .addKeys("id")
            .newResultSetExtractor(User.class);

    @Autowired
    public UserJdbcDao (final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(JdbcQueries.USER_TABLE)
                .usingGeneratedKeyColumns("id")
                .usingColumns("role_id", "first_name", "last_name", "real_id", "country_id", "state_id", "city_id",
                        "aux_date", "email", "password", "phone", "linkedin", "profile_pic");
        passJdbcInsert = new SimpleJdbcInsert(dataSource)
                        .withTableName(JdbcQueries.PASSWORDS_TABLE);
    }

    /**
     * Finds a user given its id.
     * @param id The unique id for the user.
     * @return The user if found, null otherwise.
     */
    @Override
    public Optional<User> findById(long id) {
        return jdbcTemplate.query(JdbcQueries.USER_FIND_BY_ID, RESULT_SET_EXTRACTOR, id).stream().findFirst();
    }



    // TODO: REVISAR LOS PARAMETROS --> PROFILE PIC ES UN STRING??
    @Override
    public long create(String role, String firstName, String lastName, String realId, LocalDate birthDate, Location location, String email, String phone, String linkedin, String profilePicture, String password) {
        Map<String, Object> values = new HashMap<String, Object>();
        values.put("role_id", User.UserRole.valueOf(role.toUpperCase()).getId());
        values.put("first_name",firstName);
        values.put("last_name", lastName);
        values.put("real_id", realId);
        values.put("aux_date", Date.valueOf(birthDate));
        values.put("country_id", location.getCountry().getId());
        values.put("state_id", location.getState().getId());
        values.put("city_id", location.getCity().getId());
        values.put("email", email);
        values.put("phone", phone);
        values.put("linkedin", linkedin);
        values.put("profile_pic", profilePicture);
        values.put("password", password);

        return jdbcInsert.executeAndReturnKey(values).longValue();
    }

    @Override
    public long createPass(long id, String password) {
        Map<String, Object> values = new HashMap<>();
        values.put("id", id);
        values.put("password", password);

        passJdbcInsert.execute(values);
        return id;
    }


    @Override
    public Optional<User> findByUsername(String username) {

        Optional<User> user =jdbcTemplate.query(JdbcQueries.USER_FIND_BY_USERNAME , RESULT_SET_EXTRACTOR, username).stream().findFirst();
        /*if(user.isPresent()){
            user.get().setPassword(findPassword(user.get().getId()));
        }*/

        return user;
    }

    private String findPassword(long id){
        String pass = (String) jdbcTemplate.queryForObject(
                JdbcQueries.USER_FIND_PASSWORD, new Object[] { id }, String.class);
        return pass;

    }

    @Override
    public List<User> findCoincidence(String name) {
        List<User> users = jdbcTemplate.query(JdbcQueries.USER_FIND_COINCIDENCE, new Object[] {"%" + name + "%","%" + name + "%","%" + name + "%"}, RESULT_SET_EXTRACTOR);


        return users;
    }
}

