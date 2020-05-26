package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.UserAlreadyExistsException;
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
    }

    @Override
    public long create(String role, String firstName, String lastName, String realId, LocalDate birthDate, Location location,
                       String email, String phone, String linkedin, String password, byte[] imageBytes) throws UserAlreadyExistsException {

        final int roleId = User.UserRole.valueOf(role.toUpperCase()).getId();

        // Check if email already registered
        Optional<User> maybeUser = findByUsername(email);
        if (maybeUser.isPresent()) {
            if (maybeUser.get().getPassword() == null) return update(maybeUser.get().getId(), roleId, firstName, lastName, realId,
                                                                        birthDate, location, phone, linkedin, password, imageBytes);
            else throw new UserAlreadyExistsException();
        }

        // Email not registered
        Map<String, Object> values = new HashMap<>();
        values.put("role_id", roleId);
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
        if (imageBytes.length != 0) values.put("profile_pic", imageBytes);
        values.put("password", password);

        return jdbcInsert.executeAndReturnKey(values).longValue();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jdbcTemplate.query(JdbcQueries.USER_FIND_BY_USERNAME , RESULT_SET_EXTRACTOR, username).stream().findFirst();
    }

    @Override
    public Optional<User> findById(long id) {
        return jdbcTemplate.query(JdbcQueries.USER_FIND_BY_ID, RESULT_SET_EXTRACTOR, id).stream().findFirst();
    }

    @Override
    public byte[] findImageForUser(long userId) {
        return jdbcTemplate.queryForObject(JdbcQueries.USER_FIND_IMAGE_BY_ID, new Object[] {userId}, byte[].class);
    }

    @Override
    public long updateUserPassword(String username, String password) {
        return jdbcTemplate.update(JdbcQueries.USER_USERNAME_PASSWORD_UPDATE, password, username);
    }

    @Override
    public void verifyUser(String username) {
        jdbcTemplate.update(JdbcQueries.USER_SET_VERIFIED_TRUE, username);
    }

    /* Auxiliary functions */

    /**
     * Updates the existing user in database.
     * @return Operation return.
     */
    private long update(long userId, int roleId, String firstName, String lastName, String realId, LocalDate birthDate, Location location,
                        String phone, String linkedin, String password, byte[] imageBytes) {

        return jdbcTemplate.update(JdbcQueries.USER_UPDATE, roleId, firstName, lastName, realId, Date.valueOf(birthDate),
                location.getCountry().getId(), location.getState().getId(), location.getCity().getId(),
                phone, linkedin, (imageBytes.length != 0) ? imageBytes : null, password, userId);
    }
}

