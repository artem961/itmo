package web.beans;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import lombok.Data;
import web.models.StandartCalcResult;

import java.util.ArrayList;
import java.util.List;

@Named
@ApplicationScoped
@Data
public class HistoryBean {
    private List<StandartCalcResult> results;

    {
        this.results = new ArrayList<>();
    }

    public void addToHistory(StandartCalcResult result){
        this.results.add(result);
    }

    public void addToHistory(List<StandartCalcResult> results){
        this.results.addAll(results);
    }
}
