package web.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class Point {
    private BigDecimal x;
    private BigDecimal y;

    private boolean check(BigDecimal r) {
        BigDecimal zero = BigDecimal.ZERO;
        BigDecimal halfR = r.divide(BigDecimal.valueOf(2), MathContext.DECIMAL128);

        if (x.compareTo(zero) <= 0 && y.compareTo(zero) <= 0 &&
                x.compareTo(r.negate()) >= 0 && y.compareTo(halfR.negate()) >= 0) {
            return true;
        }

        if (x.compareTo(zero) >= 0 && y.compareTo(zero) >= 0) {
            BigDecimal xSquared = x.multiply(x);
            BigDecimal ySquared = y.multiply(y);
            BigDecimal rSquared = r.multiply(r);
            BigDecimal sumSquares = xSquared.add(ySquared);

            if (sumSquares.compareTo(rSquared) <= 0) {
                return true;
            }
        }

        if (x.compareTo(zero) <= 0 && y.compareTo(zero) >= 0) {
            BigDecimal func = x.multiply(BigDecimal.valueOf(2)).add(r);
            if (y.compareTo(func) <= 0) {
                return true;
            }
        }

        return false;
    }

    public StandartCalcResult checkHit(BigDecimal r) {
        Long startTime = System.nanoTime();
        boolean result = this.check(r);
        Long endTime = System.nanoTime();

        return  new StandartCalcResult(
                this.getX(),
                this.getY(),
                r,
                result,
                String.valueOf((endTime - startTime)),
                String.valueOf(LocalTime.now().withNano(0)));
    }

    public List<StandartCalcResult> checkHits(List<BigDecimal> r) {
        List<StandartCalcResult> results = new ArrayList<>();

        r.forEach((rad) -> {
            results.add(checkHit(rad));
        });
        return results;
    }
}
