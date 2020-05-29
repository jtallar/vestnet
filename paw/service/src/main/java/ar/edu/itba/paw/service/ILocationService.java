package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.daos.LocationDao;
import ar.edu.itba.paw.interfaces.services.LocationService;
import ar.edu.itba.paw.model.City;
import ar.edu.itba.paw.model.Country;
import ar.edu.itba.paw.model.Location;
import ar.edu.itba.paw.model.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Primary
@Service
public class ILocationService implements LocationService {

    @Autowired
    LocationDao locationDao;

    @Override
    public List<Country> findAllCountries() {
        return locationDao.findAllCountries();
    }

    @Override
    public List<State> findStates(long countryId) {
        return locationDao.findStates(countryId);
    }

    @Override
    public List<City> findCities(long stateId) {
        return locationDao.findCities(stateId);
    }
}
