package database.repositories;

import database.models.Result;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
@Transactional
public class ResultRepository implements Repository<Result> {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(Result entity) {
        entityManager.persist(entity);
    }

    @Override
    public void delete(Result entity) {

    }

    @Override
    public List<Result> getAll() {
        return entityManager
                .createQuery("select r from Result r", Result.class)
                .getResultList();
    }

    @Override
    public void deleteAll() {
        entityManager
                .createQuery("delete from Result r")
                .executeUpdate();
    }
}
