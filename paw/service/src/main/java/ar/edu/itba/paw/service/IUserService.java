package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.exceptions.UserAlreadyExistsException;
import ar.edu.itba.paw.interfaces.daos.UserDao;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.crypto.Data;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Optional;

@Primary
@Service
public class IUserService implements UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public User create(String role, String password, String firstName, String lastName, String realId,
                       Integer birthYear, Integer birthMonth, Integer birthDay,
                       Integer countryId, Integer stateId, Integer cityId,
                       String email, String phone, String linkedin, byte[] image) throws UserAlreadyExistsException {

        Integer roleId = User.UserRole.valueOf(role).getId();
        Country country = new Country(countryId, "", "", "", "", "");
        State state = new State(stateId, "", "");
        City city = new City(cityId, "");
        Location location = new Location(country, state, city);
        Date birthDate = new GregorianCalendar(birthYear, birthMonth, birthDay).getTime();
        return userDao.create(roleId, encoder.encode(password), firstName, lastName, realId, birthDate, location, email, phone, linkedin, image);
    }

    @Transactional
    @Override
    public Optional<User> findByUsername(String username) {
        return userDao.findByUsername(username);
    }


    @Override
    public Optional<User> findById(Long id) {
        return userDao.findById(id);
    }
}
