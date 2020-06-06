package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.TokenDao;
import ar.edu.itba.paw.model.Token;
import ar.edu.itba.paw.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.Optional;

@Repository
public class TokenJpaDao implements TokenDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Token create(User user) {
        final Token finalToken = new Token(user);
        entityManager.persist(finalToken);
        return finalToken;
    }

    @Override
    public Optional<Token> findByToken(String token) {
        final TypedQuery<Token> query = entityManager.createQuery("from Token where token = :token", Token.class);
        query.setParameter("token", token);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public int deleteExpired() {
        final TypedQuery<Token> query = entityManager.createQuery("from Token where expiryDate < :now", Token.class);
        query.setParameter("now", new Date());
        return query.executeUpdate();
    }
}
