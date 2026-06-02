package lab4.backend.data.repositories.user.postgres;

import lab4.backend.data.entities.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    UserEntity saveUser(UserEntity user);
    Optional<UserEntity> findUserByName(String name);
    Optional<UserEntity> findUserById(Integer id);
    List<UserEntity> getAllUsers();
}
