package lab1.fcgi.tools.json;

import com.google.gson.Gson;

import java.util.List;

public record CalcResultsArray(List<StandartCalcResult> results) {
    public String toJson(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
