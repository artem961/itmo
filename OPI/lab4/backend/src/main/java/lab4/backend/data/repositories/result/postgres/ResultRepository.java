package lab4.backend.data.repositories.result.postgres;

import lab4.backend.data.entities.ResultEntity;
import lab4.backend.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface ResultRepository {
    ResultEntity saveResult(ResultEntity entity);
    Optional<ResultEntity> getResultById(Integer id);
    List<ResultEntity> getAllResults();
    List<ResultEntity> getAllResultsForUser(Integer userId);
    void deleteAllResults();
    void deleteAllForUser(Integer userId);
    void deleteById(Integer id);
}
