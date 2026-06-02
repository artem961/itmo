package lab4.backend.data.repositories.user.postgres;

import jakarta.ejb.Singleton;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lab4.backend.data.entities.UserEntity;
import lombok.extern.java.Log;

import java.util.List;
import java.util.Optional;

@Stateless
@Log
public class SQLUserRepository implements UserRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public UserEntity saveUser(UserEntity user) {
        em.persist(user);
        em.flush();
        return user;
    }

    @Override
    public Optional<UserEntity> findUserByName(String name) {
        try {
            UserEntity userEntity = em
                    .createQuery("select u from UserEntity u where u.username=:username", UserEntity.class)
                    .setParameter("username", name)
                    .getSingleResult();
            return Optional.ofNullable(userEntity);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<UserEntity> findUserById(Integer id) {
        try {
            UserEntity userEntity = em.find(UserEntity.class, id);
            return Optional.ofNullable(userEntity);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<UserEntity> getAllUsers() {
        List<UserEntity> entities = em.createQuery("select u from UserEntity u", UserEntity.class).getResultList();
        return entities;
    }
}
