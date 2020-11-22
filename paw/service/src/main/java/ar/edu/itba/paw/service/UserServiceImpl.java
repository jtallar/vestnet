package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.daos.*;
import ar.edu.itba.paw.interfaces.exceptions.UserAlreadyExistsException;
import ar.edu.itba.paw.interfaces.exceptions.UserDoesNotExistException;
import ar.edu.itba.paw.interfaces.services.EmailService;
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
import java.net.URI;
import java.util.*;
import java.util.function.Consumer;

@Primary
@Service
public class UserServiceImpl implements UserService {

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

    @Autowired
    private EmailService emailService;

    @Override
    @Transactional
    public User create(User dataUser, URI baseUri) throws UserAlreadyExistsException {
        User newUser = userDao.create(dataUser);
        emailService.sendVerification(newUser, tokenDao.create(newUser).getToken(), baseUri);
        return newUser;
    }

    @Override
    @Transactional
    public Optional<User> update(long id, User dataUser) {
        Optional<User> optionalUser = findById(id);
        optionalUser.ifPresent(u -> {
            u.setLocation(dataUser.getLocation());
            u.setFirstName(dataUser.getFirstName());
            u.setLastName(dataUser.getLastName());
            u.setRealId(dataUser.getRealId());
            u.setBirthDate(dataUser.getBirthDate());
            u.setLocation(dataUser.getLocation());
            u.setPhone(dataUser.getPhone());
            u.setLinkedin(dataUser.getLinkedin());
        });
        return optionalUser;
    }

    @Override
    @Transactional
    public void remove(long id) {
        tokenDao.deleteUserTokens(new User(id));
        userDao.removeUser(id);
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
    public boolean updateVerification(String token) {
        if (token == null || token.isEmpty()) return false;

        Optional<Token> optionalToken = tokenDao.findByToken(token);
        if (!optionalToken.isPresent()) return false;

        Token realToken = optionalToken.get();
        User user = userDao.findById(realToken.getUser().getId()).get(); // Got from token, then exists

        if (!realToken.isValid()) return false;
        user.setVerified(true);
        return true;
    }


    @Override
    @Transactional
    public boolean updatePassword(String token, String password) {
        if (token == null || token.isEmpty()) return false;

        Optional<Token> optionalToken = tokenDao.findByToken(token);
        if (!optionalToken.isPresent()) return false;

        Token realToken = optionalToken.get();
        User user = userDao.findById(realToken.getUser().getId()).get(); // Got from token, then exists

        if (!realToken.isValid()) return false;
        user.setPassword(encoder.encode(password));
        return true;
    }


    @Override
    public Optional<User> requestPassword(String mail, URI baseUri) {
        Optional<User> optionalUser = userDao.findByUsername(mail);
        optionalUser.ifPresent(u -> emailService.sendPasswordRecovery(u, tokenDao.create(u).getToken(), baseUri));
        return optionalUser;
    }


    @Override
    public Optional<User> requestVerification(String mail, URI baseUri) {
        Optional<User> optionalUser = userDao.findByUsername(mail);
        optionalUser.ifPresent(u -> emailService.sendVerification(u, tokenDao.create(u).getToken(), baseUri));
        return optionalUser;
    }

    @Override
    @Transactional
    public Optional<User> updateImage(long id, byte[] image) {
        Optional<User> optionalUser = userDao.findById(id);
        optionalUser.ifPresent(u -> {
            UserImage userImage = null;
            Optional<UserImage> optionalImage;
            Long imageId = u.getImage_id();
            if (imageId == null || !(optionalImage = imageDao.findUserImage(imageId)).isPresent()) {
                if (image.length > 0) userImage = imageDao.create(image);
            } else {
                userImage = optionalImage.get();
                userImage.setImage(image);
            }

            u.setImage(userImage);
        });
        return optionalUser;
    }


    @Override
    @Transactional
    public Optional<User> setLocale(String username, String locale) {
        Optional<User> optionalUser = userDao.findByUsername(username);
        optionalUser.ifPresent(u -> {
            if (!locale.equals(u.getLocale()))
                u.setLocale(locale);
        });
        return optionalUser;
    }


    @Override
    @Transactional
    public Optional<User> favorites(long userId, long projectId, boolean add) {
        Optional<User> optionalUser = userDao.findById(userId);
        optionalUser.ifPresent(u -> {
            if (add) u.getFavorites().add(new Project(projectId));
            else u.getFavorites().remove(new Project(projectId));
        });
        return optionalUser;
    }


    @Override
    public List<Project> getOwnedProjects(long id, boolean funded) {
        List<FilterCriteria> param = new ArrayList<>();
        param.add(new FilterCriteria("owner", new User(id)));
        param.add(new FilterCriteria("funded", funded));
        return projectDao.findAll(param, OrderField.DEFAULT);
    }


    @Override
    public Page<Message> getAcceptedMessages(long receiverId, int page, int pageSize) {
        List<FilterCriteria> filters = new ArrayList<>();
        filters.add(new FilterCriteria("receiver", new User(receiverId)));
        filters.add(new FilterCriteria("accepted", true));
        return messageDao.findAll(filters, OrderField.DATE_DESCENDING, new PageRequest(page, pageSize));
    }


    @Override
    public Page<Message> getOfferMessages(long senderId, int page, int pageSize) {
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
    public Optional<UserImage> getProfileImage(long id) {
        return imageDao.findUserImage(id);
    }
}
