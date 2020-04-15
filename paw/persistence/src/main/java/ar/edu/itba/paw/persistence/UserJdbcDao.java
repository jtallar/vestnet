package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.model.Location;
import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.net.URI;
import java.util.*;


@Repository
public class UserJdbcDao implements UserDao {
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    final static RowMapper<User> ROW_MAPPER = (rs, rowNum) ->
            new User(rs.getLong("id"), rs.getString("first_name"),
            rs.getString("last_name"), rs.getString("real_id"), rs.getDate("aux_date"),
            new Location(new Location.Country(rs.getInt("country_id"), rs.getString("country"), rs.getString("iso2"), rs.getString("phonecode"), rs.getString("currency")),
                    new Location.State(rs.getInt("state_id"),rs.getString("state"), rs.getString("iso2")),
                    new Location.City(rs.getInt("city_id"), rs.getString("city"))),
            rs.getString("email"), rs.getString("phone"), rs.getString("linkedin"),
            rs.getString("profile_pic"),rs.getDate("join_date"), rs.getInt("trust_index"));

    @Autowired
    public UserJdbcDao (final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
    }

    // TODO: REVISAR. NO CONVIENE HACERLO POR SEPARADO EL MAPEO DEL LOCATION?
    @Override
    public Optional<User> findById(long id) {
        return jdbcTemplate.query(
                "SELECT *" +
                        "FROM users JOIN countries ON (users.country_id = countries.id) " +
                        "JOIN cities ON (users.city_id = cities.id) " +
                        "JOIN states ON (users.state_id = states.id) " +
                        "WHERE users.id = ? "
                , new Object[] {id}, ROW_MAPPER)
                .stream().findFirst();
    }
    /*
    public Optional<User> findByUsername(String username) {
        return jdbcTemplate.query("SELECT * FROM users WHERE username = ?", ROW_MAPPER, username)
                .stream().findFirst();
    }

     */

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
}

