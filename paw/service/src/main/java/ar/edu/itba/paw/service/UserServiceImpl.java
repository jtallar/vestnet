package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.Location;
import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Primary
@Service
public class UserServiceImpl implements UserService {

    //@Autowired passwordEncoder encoder

    //TODO< encode password

    @Autowired
    private UserDao userDao;

    @Override
    public Optional<User> findById(long id) {
        return userDao.findById(id);
    }

    @Override
    public User create(String firstName, String lastName, String realId, Date birthDate, Location location, String email, String phone, String linkedin, String profilePicture, Date joinDate, int trustIndex) {
        return userDao.create(firstName,lastName,realId,birthDate,location,email,phone,linkedin,profilePicture,joinDate,trustIndex);
    }


    @Override
    public Optional<User> findByUsername(String username) {
        return userDao.findByUsername(username);
    }
}
