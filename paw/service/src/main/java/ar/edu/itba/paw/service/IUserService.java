package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.daos.TokenDao;
import ar.edu.itba.paw.interfaces.exceptions.UserAlreadyExistsException;
import ar.edu.itba.paw.interfaces.daos.UserDao;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.image.UserImage;
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
    private TokenDao tokenDao;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    @Transactional
    public User create(String role, String password, String firstName, String lastName, String realId,
                       Integer birthYear, Integer birthMonth, Integer birthDay,
                       Integer countryId, Integer stateId, Integer cityId,
                       String email, String phone, String linkedin, long imageId) throws UserAlreadyExistsException {

        Integer roleId = User.UserRole.getEnum(role).getId();
        Country country = new Country(countryId, "", "", "", "", "");
        State state = new State(stateId, "", "");
        City city = new City(cityId, "");
        Location location = new Location(country, state, city);
        Date birthDate = new GregorianCalendar(birthYear, birthMonth, birthDay).getTime();
        return userDao.create(roleId, encoder.encode(password), firstName, lastName, realId, birthDate, location, email, phone, linkedin, new UserImage(imageId));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userDao.findByUsername(username);
    }


    @Override
    public Optional<User> findById(long id) {
        return userDao.findById(id);
    }

    @Override
    @Transactional
    public User updatePassword(String mail, String password) {
        Optional<User> userOptional = userDao.findByUsername(mail);
        if (!userOptional.isPresent()) return null;

        User user = userOptional.get();
        user.setPassword(encoder.encode(password));
        return user;
    }

    @Override
    @Transactional
    public User verifyUser(long id) {
        Optional<User> userOptional = userDao.findById(id);
        if (!userOptional.isPresent()) return null;

        User user = userOptional.get();
        user.setVerified(true);
        return user;
    }

    @Override
    @Transactional
    public User deleteFavorite(long userId, long projectId) {
        Optional<User> userOptional = userDao.findById(userId);
        if (!userOptional.isPresent()) return null;

        User user = userOptional.get();
        List<Project> favorites = user.getFavorites();
        favorites.remove(new Project(projectId));
        return user;
    }

    @Override
    @Transactional
    public User addFavorite(long userId, long projectId) {
        Optional<User> userOptional = userDao.findById(userId);
        if (!userOptional.isPresent()) return null;

        User user = userOptional.get();
        List<Project> favorites = user.getFavorites();
        favorites.add(new Project(projectId));
        return user;
    }

    @Override
    @Transactional
    public Token createToken(long userId) {
        return tokenDao.create(new User(userId));
    }

    @Override
    @Transactional
    public Optional<Token> findToken(String token) {
        return tokenDao.findByToken(token);
    }
}
