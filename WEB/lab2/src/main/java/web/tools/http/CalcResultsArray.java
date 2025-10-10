package web.tools.http;

import com.google.gson.Gson;
import lombok.Getter;

import java.util.List;

public record CalcResultsArray(List<StandartCalcResult> results) {
    public String toJson(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
