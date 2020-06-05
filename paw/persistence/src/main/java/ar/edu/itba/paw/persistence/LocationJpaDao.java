package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.LocationDao;
import ar.edu.itba.paw.model.location.City;
import ar.edu.itba.paw.model.location.Country;
import ar.edu.itba.paw.model.location.State;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class LocationJpaDao implements LocationDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Country> findAllCountries() {
        final TypedQuery<Country> query = entityManager.createQuery("from Country order by name", Country.class);
        List<Country> countries = query.getResultList();
        countries.remove(0);
        return countries;
    }

    @Override
    public List<State> findStates(long countryId) {
        final TypedQuery<State> query = entityManager.createQuery("from State where country_id = :countryId order by name", State.class);
        query.setParameter("countryId", countryId);
        return query.getResultList();
    }

    @Override
    public List<City> findCities(long stateId) {
        final TypedQuery<City> query = entityManager.createQuery("from City where state_id = :stateId order by name", City.class);
        query.setParameter("stateId", stateId);
        return query.getResultList();
    }
}
