package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.UserAlreadyExistsException;
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
import java.util.List;
import java.util.Optional;

@Primary
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public Optional<User> findById(long id) {
        return userDao.findById(id);
    }

    @Override
    public long create(String role, String firstName, String lastName, String realId, LocalDate birthDate, Location location,
                       String email, String phone, String linkedin, String password, byte[] imageBytes) throws UserAlreadyExistsException {
        return userDao.create(role, firstName,lastName,realId,birthDate,location,email,phone,linkedin, encoder.encode(password), imageBytes);
    }

    @Override
    public long createPassword(long id, String password) {
        return userDao.createPass(id, encoder.encode(password));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public List<User> findCoincidence(String name) {
        return userDao.findCoincidence(name);
    }

    @Override
    public byte[] findImageForUser(long userId) {
        return userDao.findImageForUser(userId);
    }
}
