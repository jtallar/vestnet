package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.daos.*;
import ar.edu.itba.paw.interfaces.exceptions.UserAlreadyExistsException;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.components.*;
import ar.edu.itba.paw.model.image.UserImage;
import ar.edu.itba.paw.model.location.City;
import ar.edu.itba.paw.model.location.Country;
import ar.edu.itba.paw.model.location.Location;
import ar.edu.itba.paw.model.location.State;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

@Primary
@Service
public class IUserService implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private ImageDao imageDao;

    @Autowired
    private TokenDao tokenDao;

    @Autowired
    private PasswordEncoder encoder;


    @Override
    @Transactional
    public User create(String role, String password, String firstName, String lastName, String realId,
                       Integer birthYear, Integer birthMonth, Integer birthDay,
                       Integer countryId, Integer stateId, Integer cityId,
                       String email, String phone, String linkedin, byte[] image) throws UserAlreadyExistsException {

        UserImage userImage = imageDao.create(image);

        Integer roleId = UserRole.getEnum(role).getId();
        Country country = new Country(countryId, "", "", "", "", "");
        State state = new State(stateId, "", "");
        City city = new City(cityId, "");
        Location location = new Location(country, state, city);
        Date birthDate = new GregorianCalendar(birthYear, birthMonth, birthDay).getTime();
        return userDao.create(roleId, encoder.encode(password), firstName, lastName, realId, birthDate, location, email, phone, linkedin, userImage);
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
    public List<Project> getOwnedProjects(long id) {
        List<FilterCriteria> param = Collections.singletonList(new FilterCriteria("owner", new User(id)));
        return projectDao.findAll(param, OrderField.DEFAULT);
    }


    @Override
    public Page<Message> getAcceptedMessages(long receiverId, Integer page, Integer pageSize) {
        List<FilterCriteria> filters = new ArrayList<>();
        filters.add(new FilterCriteria("receiver", new User(receiverId)));
        filters.add(new FilterCriteria("accepted", true));
        return messageDao.findAll(filters, OrderField.DATE_DESCENDING, new PageRequest(page, pageSize));
    }


    @Override
    public Page<Message> getOfferMessages(long senderId, Integer page, Integer pageSize) {
        List<FilterCriteria> filters = Collections.singletonList(new FilterCriteria("sender", new User(senderId)));
        return messageDao.findAll(filters, OrderField.DATE_DESCENDING, new PageRequest(page, pageSize));
    }


    @Override
    public List<Message> getProjectUnreadMessages(long userId, long projectId) {
        List<FilterCriteria> filters = new ArrayList<>();
        filters.add(new FilterCriteria("receiver", new User(userId)));
        filters.add(new FilterCriteria("project", new Project(projectId)));
        filters.add(new FilterCriteria("unread", true));
        return messageDao.findAll(filters, OrderField.DATE_DESCENDING);
    }


    @Override
    public Optional<Message> getLastProjectOfferMessage(long userId, long projectId) {
        List<FilterCriteria> filters = new ArrayList<>();
        filters.add(new FilterCriteria("sender", new User(userId)));
        filters.add(new FilterCriteria("project", new Project(projectId)));
        return messageDao.findAll(filters, OrderField.DATE_DESCENDING).stream().findFirst();
    }


    @Override
    public byte[] getProfileImage(long id) {
        Optional<UserImage> optionalImage = imageDao.findUserImage(id);
        if (optionalImage.isPresent()) return optionalImage.get().getImage();

        byte[] image = new byte[0];
        try {
            Resource stockImage = new ClassPathResource("userNoImage.png");
            image = IOUtils.toByteArray(stockImage.getInputStream());
        } catch (IOException e) {
            return image;
        }
        return image;
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
