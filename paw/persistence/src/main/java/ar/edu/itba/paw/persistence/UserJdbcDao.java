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
import java.util.*;


@Repository
public class UserJdbcDao implements UserDao {
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    private final static ResultSetExtractor<List<User>> RESULT_SET_EXTRACTOR = JdbcTemplateMapperFactory
            .newInstance()
            .addKeys("id")
            .newResultSetExtractor(User.class);

    @Autowired
    public UserJdbcDao (final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(JdbcQueries.USER_TABLE)
                .usingGeneratedKeyColumns("id");
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



    // TODO: REVISAR LOS PARAMETROS --> SEPARAR EN ENTREPENEUR Y INVESTOR
    //                              --> SACAR trust index, joinDate
//                                  --> PROFILE PIC ES UN STRING??
//                                  --> id es el role_id?
    @Override
    public User create(long id, String firstName, String lastName, String realId, Date birthDate, Location location, String email, String phone, String linkedin, String profilePicture, Date joinDate, int trustIndex) {
        Map<String, Object> values = new HashMap<String, Object>();
        values.put("role_id", id);
        values.put("first_name",firstName);
        values.put("last_name", lastName);
        values.put("real_id", realId);
        values.put("aux_date", birthDate);
        values.put("country_id", location.getCountry().getId());
        values.put("state_id", location.getState().getId());
        values.put("city_id", location.getCity().getId());
        values.put("email", email);
        values.put("phone", phone);
        values.put("linkedin", linkedin);
        values.put("profile_pic", profilePicture);
        values.put("join_date", joinDate);
        values.put("trust_index", trustIndex);


        Number keyNumber = jdbcInsert.executeAndReturnKey(values);

        return new User(keyNumber.longValue(),firstName,lastName,realId,birthDate,location,email,phone,linkedin,profilePicture,joinDate,trustIndex);
    }



    @Override
    public Optional<User> findByUsername(String username) {
        return findById(4); //just for testing
    }
}

