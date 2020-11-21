package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.daos.LocationDao;
import ar.edu.itba.paw.interfaces.services.LocationService;
import ar.edu.itba.paw.model.location.City;
import ar.edu.itba.paw.model.location.Country;
import ar.edu.itba.paw.model.location.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Primary
@Service
public class LocationServiceImpl implements LocationService {

    @Autowired
    LocationDao locationDao;


    @Override
    public List<Country> findAllCountries() {
        return locationDao.findAllCountries();
    }


    @Override
    public List<State> findStates(int countryId) {
        return locationDao.findStates(new Country(countryId));
    }


    @Override
    public List<City> findCities(int stateId) {
        return locationDao.findCities(new State(stateId));
    }
}
