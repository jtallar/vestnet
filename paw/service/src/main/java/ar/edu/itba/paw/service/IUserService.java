package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.exceptions.UserAlreadyExistsException;
import ar.edu.itba.paw.interfaces.daos.UserDao;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.location.City;
import ar.edu.itba.paw.model.location.Country;
import ar.edu.itba.paw.model.location.Location;
import ar.edu.itba.paw.model.location.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Primary
@Service
public class IUserService implements UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    @Transactional
    public User create(String role, String password, String firstName, String lastName, String realId,
                       Integer birthYear, Integer birthMonth, Integer birthDay,
                       Integer countryId, Integer stateId, Integer cityId,
                       String email, String phone, String linkedin) throws UserAlreadyExistsException {

        Integer roleId = User.UserRole.getEnum(role).getId();
        Country country = new Country(countryId, "", "", "", "", "");
        State state = new State(stateId, "", "");
        City city = new City(cityId, "");
        Location location = new Location(country, state, city);
        Date birthDate = new GregorianCalendar(birthYear, birthMonth, birthDay).getTime();
        return userDao.create(roleId, encoder.encode(password), firstName, lastName, realId, birthDate, location, email, phone, linkedin);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userDao.findByUsername(username);
    }


    @Override
    public Optional<User> findById(Long id) {
        return userDao.findById(id);
    }

    @Override
    public User deleteFavorite(Long userId, Long projectId) {
        Optional<User> userOptional = userDao.findById(userId);
        if (!userOptional.isPresent()) return null;

        User user = userOptional.get();
        List<Project> favorites = user.getFavorites();
        favorites.remove(new Project(projectId));
        return user;
    }

    @Override
    public User addFavorite(Long userId, Long projectId) {
        Optional<User> userOptional = userDao.findById(userId);
        if (!userOptional.isPresent()) return null;

        User user = userOptional.get();
        List<Project> favorites = user.getFavorites();
        favorites.add(new Project(projectId));
        return user;
    }
}
