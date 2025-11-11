package web.models;

import java.math.BigDecimal;

public record StandartCalcResult(BigDecimal x,
                                 BigDecimal y,
                                 BigDecimal r,
                                 boolean result,
                                 String time,
                                 String currentTime) implements Comparable<StandartCalcResult> {


    @Override
    public int compareTo(StandartCalcResult o) {
        return this.currentTime.compareTo(o.currentTime);
    }
}
