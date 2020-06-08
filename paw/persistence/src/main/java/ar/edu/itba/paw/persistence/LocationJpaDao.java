package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.LocationDao;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.location.City;
import ar.edu.itba.paw.model.location.Country;
import ar.edu.itba.paw.model.location.State;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class LocationJpaDao implements LocationDao {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<Country> findAllCountries() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Country> query = builder.createQuery(Country.class);
        Root<Country> root = query.from(Country.class);

        query.orderBy(builder.asc(root.get("name")));
        List<Country> countries = entityManager.createQuery(query).getResultList();
        countries.remove(0);
        return countries;
    }


    @Override
    public List<State> findStates(Country parent) {
        final TypedQuery<State> query = entityManager.createQuery("from State where country_id = :countryId order by name", State.class);
        query.setParameter("countryId", parent.getId());
        return query.getResultList();
    }


    @Override
    public List<City> findCities(State parent) {
        final TypedQuery<City> query = entityManager.createQuery("from City where state_id = :stateId order by name", City.class);
        query.setParameter("stateId", parent.getId());
        return query.getResultList();
    }
}
