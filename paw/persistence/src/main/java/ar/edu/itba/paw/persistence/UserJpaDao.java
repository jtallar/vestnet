package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.UserDao;
import ar.edu.itba.paw.interfaces.exceptions.UserAlreadyExistsException;
import ar.edu.itba.paw.model.Location;
import ar.edu.itba.paw.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Repository
public class UserJpaDao implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User create(Integer role, String password, String firstName, String lastName, String realId, Date birthDate,
                       Location location, String email, String phone, String linkedin, byte[] image) throws UserAlreadyExistsException {
        // TODO make JPA do it by itself
        if (findByUsername(email).isPresent()) throw new UserAlreadyExistsException();
        final User user = new User(role, password, firstName, lastName, realId, birthDate, location, email, phone, linkedin, image);
        entityManager.persist(user);
        return user;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        final TypedQuery<User> query = entityManager.createQuery("from User where email = :username", User.class);
        query.setParameter("username", username);
        Optional<User> user = query.getResultList().stream().findFirst();
        System.out.println("MATI ACA: " + user);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }
}
