package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.LocationDao;
import ar.edu.itba.paw.interfaces.LocationService;
import ar.edu.itba.paw.model.Location;
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
    public List<Location.Country> findAllCountries() {
        return locationDao.findAllCountries();
    }

    @Override
    public List<Location.State> findStates(long countryId) {
        return locationDao.findStates(countryId);
    }

    @Override
    public List<Location.City> findCities(long stateId) {
        return locationDao.findCities(stateId);
    }
}
