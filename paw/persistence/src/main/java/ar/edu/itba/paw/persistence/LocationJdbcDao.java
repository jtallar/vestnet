package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.LocationDao;
import ar.edu.itba.paw.model.Location.City;
import ar.edu.itba.paw.model.Location.Country;
import ar.edu.itba.paw.model.Location.State;
import org.simpleflatmapper.jdbc.spring.JdbcTemplateMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class LocationJdbcDao implements LocationDao {

    private JdbcTemplate jdbcTemplate;

    private final static ResultSetExtractor<List<Country>> RESULT_SET_EXTRACTOR_COUNTRY = JdbcTemplateMapperFactory
            .newInstance()
            .addKeys("id")
            .newResultSetExtractor(Country.class);

    private final static ResultSetExtractor<List<State>> RESULT_SET_EXTRACTOR_STATE = JdbcTemplateMapperFactory
            .newInstance()
            .addKeys("id")
            .newResultSetExtractor(State.class);

    private final static ResultSetExtractor<List<City>> RESULT_SET_EXTRACTOR_CITY = JdbcTemplateMapperFactory
            .newInstance()
            .addKeys("id")
            .newResultSetExtractor(City.class);

    @Autowired
    public LocationJdbcDao (final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Override
    public List<Country> findAllCountries() {
        return jdbcTemplate.query(JdbcQueries.COUNTRY_FIND_ALL, RESULT_SET_EXTRACTOR_COUNTRY);
    }

    @Override
    public List<State> findStates(long countryId) {
        return jdbcTemplate.query(JdbcQueries.STATE_FIND_BY_COUNTRY_ID, RESULT_SET_EXTRACTOR_STATE, countryId);
    }

    @Override
    public List<City> findCities(long stateId) {
        return jdbcTemplate.query(JdbcQueries.CITY_FIND_BY_STATE_ID, RESULT_SET_EXTRACTOR_CITY, stateId);
    }
}
