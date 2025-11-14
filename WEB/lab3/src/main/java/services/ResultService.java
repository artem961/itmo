package services;

import database.models.Result;
import database.repositories.ResultRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import web.models.StandartCalcResult;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ResultService {
    @Inject
    private ResultRepository resultRepository;

    public void save(StandartCalcResult result) {
        Result resultEntity = new Result();
        resultEntity.setX(result.x());
        resultEntity.setY(result.y());
        resultEntity.setR(result.r());
        resultEntity.setResult(result.result());
        resultEntity.setTime(result.time());
        resultEntity.setCurrentTime(result.currentTime());

        resultRepository.save(resultEntity);
    }

    public List<StandartCalcResult> getAll() {
        List<Result> resultEntities = resultRepository.getAll();
        List<StandartCalcResult> results = new ArrayList<>();

        resultEntities.forEach(entity -> {
            results.add(new StandartCalcResult(
                    entity.getX(),
                    entity.getY(),
                    entity.getR(),
                    entity.isResult(),
                    entity.getTime(),
                    entity.getCurrentTime()
            ));
        });

        return results;
    }

    public void removeAll(){
        resultRepository.deleteAll();
    }
}
