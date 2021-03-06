package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.daos.*;
import ar.edu.itba.paw.interfaces.exceptions.InvalidTokenException;
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

        /** Check for unique mail */
        if (userDao.findByUsername(dataUser.getEmail()).isPresent()) throw new UserAlreadyExistsException();
        dataUser.setPassword(encoder.encode(dataUser.getPassword()));

        /** Persist user */
        final User newUser = userDao.create(dataUser);

        /** Send verification email */
        emailService.sendVerification(newUser, tokenDao.create(newUser).getToken(), baseUri);

        return newUser;
    }

    @Override
    @Transactional
    public Optional<User> update(long id, User dataUser) {
        final Optional<User> optionalUser = findById(id);
        optionalUser.ifPresent(u -> {
            u.setFirstName(dataUser.getFirstName());
            u.setLastName(dataUser.getLastName());
            u.setRealId(dataUser.getRealId());
            u.setBirthDate(dataUser.getBirthDate());
            u.setPhone(dataUser.getPhone());
            u.setLinkedin(dataUser.getLinkedin());
        });
        return optionalUser;
    }


    @Override
    @Transactional
    public void remove(long id) {
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
    public Optional<User> addFavorites(long userId, long projectId, boolean add) {
        final Optional<User> optionalUser = userDao.findById(userId);
        optionalUser.ifPresent(u -> {
            if (add) u.getFavoriteProjects().add(new Project(projectId));
            else u.getFavoriteProjects().remove(new Project(projectId));
        });
        return optionalUser;
    }


    @Override
    public Page<Project> getOwnedProjects(long id, boolean closed, int page, int pageSize) {
        final RequestBuilder request = new ProjectRequestBuilder()
                .setOwner(id)
                .setClosed(closed)
                .setOrder(OrderField.PROJECT_DEFAULT);
        return projectDao.findAll(request, new PageRequest(page, pageSize));
    }


    @Override
    @Transactional
    public Optional<User> requestPassword(String mail, URI baseUri) {
        final Optional<User> optionalUser = userDao.findByUsername(mail);
        optionalUser.ifPresent(u -> emailService.sendPasswordRecovery(u, tokenDao.create(u).getToken(), baseUri));
        return optionalUser;
    }


    @Override
    @Transactional
    public Optional<User> requestVerification(String mail, URI baseUri) throws UserAlreadyExistsException {
        final Optional<User> optionalUser = userDao.findByUsername(mail);
        if (optionalUser.isPresent()) {
            if (optionalUser.get().isVerified()) throw new UserAlreadyExistsException();
            emailService.sendVerification(optionalUser.get(), tokenDao.create(optionalUser.get()).getToken(), baseUri);
        }
        return optionalUser;
    }


    @Override
    @Transactional
    public void updatePassword(String token, String password) throws InvalidTokenException {
        updateWithToken(token, password, true);
    }


    @Override
    @Transactional
    public void updateVerification(String token, URI baseUri) throws InvalidTokenException {
        updateWithToken(token, null, false);
    }


    @Override
    public Optional<UserImage> getImage(long id) {
        return imageDao.findUserImage(id);
    }


    @Override
    @Transactional
    public Optional<User> setImage(long id, byte[] image) {
        final Optional<User> optionalUser = userDao.findById(id);
        optionalUser.ifPresent(u -> {
            UserImage userImage = null;
            Optional<UserImage> optionalImage;
            Long imageId = u.getImageId();
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
        final Optional<User> optionalUser = userDao.findByUsername(username);
        optionalUser.ifPresent(u -> {
            if (!locale.equals(u.getLocale()))
                u.setLocale(locale);
        });
        return optionalUser;
    }



    /** Auxiliary functions */


    /**
     * Updates the password or sets as verified.
     * @param token The necessary token to check its validity.
     * @param password The password, if given, to update.
     * @param isPassword If its change of password or verification.
     * @throws InvalidTokenException When the token corrupted/missing/invalid.
     */
    private void updateWithToken(String token, String password, boolean isPassword) throws InvalidTokenException {
        if (token == null || token.isEmpty()) throw new InvalidTokenException("Empty token string");

        final Optional<Token> optionalToken = tokenDao.findByToken(token);
        if (!optionalToken.isPresent()) throw new InvalidTokenException("Token not found");

        final Token realToken = optionalToken.get();
        final User user = userDao.findById(realToken.getUser().getId()).get(); // Got from token, then exists

        /** In any case if its valid or not the token is no longer needed */
        tokenDao.delete(realToken.getId());

        /** Resend in case of an invalid token for verification */
        if (!realToken.isValid()) throw new InvalidTokenException("Token invalid");

        /** Valid token, make changes */
        if (isPassword) user.setPassword(encoder.encode(password));
        else user.setVerified(true);
    }
}
