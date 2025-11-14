package web.beans;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Data;
import lombok.extern.java.Log;
import services.ResultService;
import web.models.CalcResultsArray;
import web.models.StandartCalcResult;

import java.util.ArrayList;
import java.util.List;

@Named
@ApplicationScoped
@Data
@Log
public class HistoryBean {
    @Inject
    private ResultService resultService;
    private List<StandartCalcResult> resultsCache;

    {
        this.resultsCache = new ArrayList<>();
    }

    public void addToHistory(StandartCalcResult result){
        this.resultsCache.add(result);
        resultService.save(result);
    }

    public void addToHistory(List<StandartCalcResult> results){
        this.resultsCache.addAll(results);
        results.forEach(resultService::save);
    }

    public String getResultsAsJson(){
        CalcResultsArray calcResultsArray = new CalcResultsArray(resultsCache);
        return calcResultsArray.toJson();
    }

    public List<StandartCalcResult> getHistory(){
        return resultsCache;
    }

    public void clearHistory(){
        resultsCache.clear();
        resultService.removeAll();
    }
}
