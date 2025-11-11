package web.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.MathContext;

@Getter
@AllArgsConstructor
public class Point {
    private BigDecimal x;
    private BigDecimal y;

    public boolean checkHit(BigDecimal r) {
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
}
