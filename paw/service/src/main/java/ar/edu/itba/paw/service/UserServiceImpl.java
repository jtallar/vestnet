package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.Location;
import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

@Primary
@Service
public class UserServiceImpl implements UserService {

    //@Autowired passwordEncoder encoder

    //TODO< encode password

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public Optional<User> findById(long id) {
        return userDao.findById(id);
    }

    @Override
    public long create(String role, String firstName, String lastName, String realId, LocalDate birthDate, Location location, String email, String phone, String linkedin, String profilePicture, String password) {
        return userDao.create(role, firstName,lastName,realId,birthDate,location,email,phone,linkedin,profilePicture, encoder.encode(password));
    }

    @Override
    public long createPassword(long id, String password) {
        return userDao.createPass(id, encoder.encode(password));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userDao.findByUsername(username);
    }


}
