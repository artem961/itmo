package web.beans;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Data;
import services.ResultService;
import web.models.CalcResultsArray;
import web.models.StandartCalcResult;

import java.util.ArrayList;
import java.util.List;

@Named
@ApplicationScoped
@Data
public class HistoryBean {
    @Inject
    private ResultService resultService;
    private List<StandartCalcResult> results;

    {
        this.results = new ArrayList<>();
    }

    public void addToHistory(StandartCalcResult result){
        this.results.add(result);
    }

    public void addToHistory(List<StandartCalcResult> results){
        this.results.addAll(results);
        this.results.forEach(resultService::save);

    }

    public String getResultsAsJson(){
        CalcResultsArray calcResultsArray = new CalcResultsArray(results);
        return calcResultsArray.toJson();
    }

    public void clearHistory(){
        this.results.clear();
    }
}
