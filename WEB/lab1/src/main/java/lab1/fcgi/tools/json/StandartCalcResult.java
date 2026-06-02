package lab1.fcgi.tools.json;

import com.google.gson.Gson;

import java.math.BigDecimal;

public record StandartCalcResult(BigDecimal x,
                                 BigDecimal y,
                                 BigDecimal r,
                                 boolean result,
                                 String time,
                                 String currentTime){
    public String toJson(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
