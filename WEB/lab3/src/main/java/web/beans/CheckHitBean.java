package web.beans;


import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import lombok.Data;
import lombok.extern.java.Log;
import web.models.Point;
import web.models.StandartCalcResult;


import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

@Named
@RequestScoped
@Data
@Log
public class CheckHitBean {
    private BigDecimal y;
    private BigDecimal x;
    private List<BigDecimal> r;
    private List<StandartCalcResult> results;
    private String message;

    public void calcResults() {
        try {
            log.info(y.toString() + " " + x.toString() + " " + Arrays.toString(r.toArray()));
            Point point = new Point(this.x, this.y);
            this.results = checkHits(point, this.r);
            log.info(Arrays.toString(this.results.toArray()));
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString(), e);
            this.message = e.getMessage();
        }
    }

    private List<StandartCalcResult> checkHits(Point point, List<BigDecimal> r) {
        List<StandartCalcResult> results = new ArrayList<>();

        r.forEach((rad) -> {
            Long startTime = System.nanoTime();
            boolean result = point.checkHit(rad);
            Long endTime = System.nanoTime();

            StandartCalcResult standartResult = new StandartCalcResult(
                    point.getX(),
                    point.getY(),
                    rad,
                    result,
                    String.valueOf((endTime - startTime)),
                    String.valueOf(LocalTime.now().withNano(0)));
            results.add(standartResult);
        });
        return results;
    }
}
