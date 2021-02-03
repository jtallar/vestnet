package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.TokenDao;
import ar.edu.itba.paw.model.Token;
import ar.edu.itba.paw.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
    public void delete(long id) {
        Optional.ofNullable(entityManager.find(Token.class, id)).ifPresent(token -> entityManager.remove(token));
    }


    @Override
    public Optional<Token> findByToken(String token) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Token> query = builder.createQuery(Token.class);
        Root<Token> root = query.from(Token.class);

        query.where(builder.like(root.get("token"), token));
        return entityManager.createQuery(query).getResultList().stream().findFirst();
    }


    @Override
    public int deleteExpired() {
        final TypedQuery<Token> query = entityManager.createQuery("delete from Token where expiryDate < :now", Token.class);
        query.setParameter("now", new Date());
        return query.executeUpdate();
    }


    @Override
    public int deleteUserTokens(User user) {
        final TypedQuery<Token> query = entityManager.createQuery("delete from Token where user = :user", Token.class);
        query.setParameter("user", user);
        return query.executeUpdate();
    }
}
