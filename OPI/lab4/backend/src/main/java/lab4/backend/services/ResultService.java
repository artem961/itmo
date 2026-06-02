package lab4.backend.services;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lab4.backend.data.entities.ResultEntity;
import lab4.backend.data.repositories.result.postgres.ResultRepository;
import lab4.backend.dto.ResultDTO;
import lab4.backend.dto.DotDTO;
import lab4.backend.dto.UserDTO;
import lab4.backend.services.exceptions.ServiceException;
import lab4.backend.services.utils.ResultsObserver;
import lab4.backend.services.utils.annotations.ExceptionMessage;
import lab4.backend.services.utils.annotations.WrapWithServiceException;
import lab4.backend.services.utils.HitChecker;
import lab4.backend.utils.ClickInterval;
import lab4.backend.utils.HitsCounter;
import lab4.backend.utils.mapping.ResultMapper;
import lab4.backend.utils.observer.Observer;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import org.eclipse.persistence.jpa.jpql.parser.DateTime;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
@WrapWithServiceException
@ExceptionMessage
@Log
public class ResultService {
    @EJB
    private ResultRepository resultRepository;

    @Inject
    private HitsCounter hitsCounter;

    @Inject
    private ClickInterval clickInterval;

    @EJB
    private Observer resultsObserver;

    @Transactional
    public ResultDTO checkHit(DotDTO dotDTO, UserDTO user) {
        ResultDTO resultDTO = HitChecker.checkHit(dotDTO);
        resultDTO.setUser(user);
        ResultEntity entity = resultRepository.saveResult(
                ResultMapper.dtoToEntity(resultDTO));

        hitsCounter.addPoint(resultDTO.getResult());
        clickInterval.recordClick();
        resultsObserver.notifyListeners();
        return ResultMapper.entityToDTO(entity);
    }

    @Transactional
    public List<ResultDTO> getAllResults() {
        return resultRepository.getAllResults().stream()
                .map(ResultMapper::entityToDTO)
                .collect(Collectors.toList());
    }

    public List<ResultDTO> getAllResultsForUser(UserDTO user) {
        return resultRepository.getAllResultsForUser(user.getId()).stream()
                .map(ResultMapper::entityToDTO)
                .collect(Collectors.toList());
    }

    public void deleteAllResults() {
        resultRepository.deleteAllResults();
        resultsObserver.notifyListeners();
    }

    public void deleteAllResultsForUser(UserDTO user) {
        resultRepository.deleteAllForUser(user.getId());
        resultsObserver.notifyListeners();
    }

    @Transactional
    public void deleteResultById(UserDTO user, Integer id) {
        ResultEntity result = resultRepository.getResultById(id)
                .orElseThrow(() -> new ServiceException(String.format("Result with id %s not found", id)));

        if (result.getUser().getId().equals(user.getId())) {
            resultRepository.deleteById(id);
            resultsObserver.notifyListeners();
        } else {
            throw new ServiceException(String.format("User %s is not owner of result with id %s", user.getUsername(), id));
        }
    }
}
