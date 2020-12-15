package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.daos.*;
import ar.edu.itba.paw.interfaces.exceptions.UserAlreadyExistsException;
import ar.edu.itba.paw.interfaces.services.EmailService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.components.*;
import ar.edu.itba.paw.model.enums.OrderField;
import ar.edu.itba.paw.model.image.UserImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.*;

@Primary
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ProjectDao projectDao;

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
        dataUser.setPassword(encoder.encode(dataUser.getPassword()));
        User newUser = userDao.create(dataUser);
        emailService.sendVerification(newUser, tokenDao.create(newUser).getToken(), baseUri);
        return newUser;
    }

    @Override
    @Transactional
    public Optional<User> update(long id, User dataUser) {
        Optional<User> optionalUser = findById(id);
        optionalUser.ifPresent(u -> {
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
    public boolean updateVerification(String token, URI baseUri) {
        return updateWithToken(token, null, false, baseUri);
    }


    @Override
    @Transactional
    public boolean updatePassword(String token, String password) {
        return updateWithToken(token, password, true, null);
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
            if (add) u.getFavorites().add(new Favorite(new User(userId), new Project(projectId)));
            else u.getFavorites().remove(new Favorite(new User(userId), new Project(projectId)));
        });
        return optionalUser;
    }


    @Override
    public List<Project> getOwnedProjects(long id, boolean closed) {
        RequestBuilder request = new ProjectRequestBuilder()
                .setOwner(id)
                .setClosed(closed)
                .setOrder(OrderField.PROJECT_DEFAULT);
        return projectDao.findAll(request);
    }


    @Override
    public Optional<UserImage> getProfileImage(long id) {
        return imageDao.findUserImage(id);
    }


    /** Auxiliary functions */

    /**
     * Updates the password or sets as verified.
     * @param token The necessary token to check its validity.
     * @param password The password, if given, to update.
     * @param isPassword If its change of password or verification.
     * @param baseUri The base uri in case the verification token has expired.
     * @return True when update is done, false if token corrupted/missing/invalid.
     */
    private boolean updateWithToken(String token, String password, boolean isPassword, URI baseUri) {
        if (token == null || token.isEmpty()) return false;

        Optional<Token> optionalToken = tokenDao.findByToken(token);
        if (!optionalToken.isPresent()) return false;

        Token realToken = optionalToken.get();
        User user = userDao.findById(realToken.getUser().getId()).get(); // Got from token, then exists

        /** Resend in case of an invalid token for verification */
        if (!realToken.isValid()) {
            if (!isPassword) emailService.sendVerification(user, tokenDao.create(user).getToken(), baseUri);
            return false;
        }

        if (isPassword) user.setPassword(encoder.encode(password));
        else user.setVerified(true);
        return true;
    }
}
