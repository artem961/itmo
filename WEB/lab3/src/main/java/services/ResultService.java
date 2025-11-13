package services;

import database.models.Result;
import database.repositories.ResultRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import web.models.StandartCalcResult;

@ApplicationScoped
@Transactional
public class ResultService {
    @Inject
    private ResultRepository resultRepository;

    public void save(StandartCalcResult result){
        Result resultEntity = new Result();
        resultEntity.setX(result.x());
        resultEntity.setY(result.y());
        resultEntity.setResult(result.result());
        resultEntity.setTime(result.time());
        resultEntity.setCurrentTime(result.currentTime());

        resultRepository.save(resultEntity);
    }
}
