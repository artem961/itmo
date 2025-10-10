package web.tools.http;

import com.google.gson.Gson;

import java.math.BigDecimal;

public record StandartCalcResult(BigDecimal x,
                                 BigDecimal y,
                                 BigDecimal r,
                                 boolean result,
                                 String time,
                                 String currentTime) implements Comparable<StandartCalcResult>{
    public String toJson(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }


    @Override
    public int compareTo(StandartCalcResult o) {
        return this.currentTime.compareTo(o.currentTime);
    }
}
