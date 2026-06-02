package lab4.backend.data.repositories.result.postgres;

import jakarta.ejb.Singleton;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import lab4.backend.data.entities.ResultEntity;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Singleton
public class SQLResultRepository implements ResultRepository {
    @PersistenceContext
    private EntityManager em;


    @Override
    public ResultEntity saveResult(ResultEntity entity) {
        em.persist(entity);
        //em.flush();

        return entity;
    }

    public Optional<ResultEntity> getResultById(Integer id) {
        try {
            return Optional.ofNullable(em.find(ResultEntity.class, id));
        } catch (EntityNotFoundException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<ResultEntity> getAllResults() {
        List<ResultEntity> results = em.createQuery(
                "SELECT r FROM ResultEntity r order by r.id desc",
                ResultEntity.class).getResultList();

        return results;
    }

    @Override
    public List<ResultEntity> getAllResultsForUser(Integer userId) {
        List<ResultEntity> results = em.createQuery(
                        "SELECT r FROM ResultEntity r WHERE r.user.id=:userId order by r.currentTime desc", ResultEntity.class)
                .setParameter("userId", userId)
                .getResultList();

        return results;
    }

    @Override
    public void deleteAllResults() {
        em.createQuery("DELETE FROM ResultEntity").executeUpdate();
    }

    @Override
    public void deleteAllForUser(Integer userId) {
        em.createQuery("DELETE FROM ResultEntity r WHERE r.user.id=:userId")
                .setParameter("userId", userId)
                .executeUpdate();
    }

    @Override
    public void deleteById(Integer id) {
        em.createQuery("DELETE FROM ResultEntity r WHERE r.id=:id")
                .setParameter("id", id)
                .executeUpdate();
    }
}
