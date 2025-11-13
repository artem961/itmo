package web.beans;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Data;
import lombok.extern.java.Log;
import web.models.Point;
import web.models.StandartCalcResult;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Named
@RequestScoped
@Data
@Log
public class CheckHitForCanvasBean {
    private BigDecimal y;
    private BigDecimal x;
    private List<BigDecimal> r;
    private String rStringList;
    private String message;
    @Inject
    private HistoryBean historyBean;

    public void calcResults() {
        try {
            List<StandartCalcResult> results = new ArrayList<>();
            parseRStringList();

            r.forEach(r -> {
                Point point = new Point(x.multiply(r), y.multiply(r));
                results.add(point.checkHit(r));
            });

            historyBean.addToHistory(results);
            this.message = "";
        } catch (Exception e) {
            this.message = e.getMessage();
        }
    }

    private void parseRStringList() {
        r = Arrays.stream(rStringList
                .split("&"))
                .map(BigDecimal::new)
                .toList();
    }
}
