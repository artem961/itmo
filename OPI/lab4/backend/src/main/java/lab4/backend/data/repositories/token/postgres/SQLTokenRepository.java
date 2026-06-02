package lab4.backend.data.repositories.token.postgres;

import jakarta.ejb.Singleton;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lab4.backend.data.entities.TokenEntity;
import lab4.backend.dto.TokenDTO;

import java.util.Optional;

@Stateless
public class SQLTokenRepository implements TokenRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public TokenEntity saveToken(TokenEntity token) {
        em.persist(token);
        em.flush();
        return token;
    }

    @Override
    public Optional<TokenEntity> findByToken(String token) {
        try {
            TokenEntity foundToken = em.createQuery(
                            "SELECT t FROM TokenEntity t WHERE t.token = :token",
                            TokenEntity.class
                    )
                    .setParameter("token", token)
                    .getSingleResult();

            return Optional.ofNullable(foundToken);

        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public void delete(TokenEntity token) {
        String tokenString = token.getToken();
        em.createQuery("delete from TokenEntity where token=:token")
                .setParameter("token", tokenString)
                .executeUpdate();
    }

    @Override
    public void update(TokenEntity token) {
        em.merge(token);
    }
}
