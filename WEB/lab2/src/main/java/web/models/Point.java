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

        if (x.compareTo(zero) >= 0 && y.compareTo(zero) >= 0 &&
                x.compareTo(r) <= 0 && y.compareTo(r.divide(BigDecimal.valueOf(2), MathContext.DECIMAL128)) <= 0) {
            return true;
        }

        if (x.compareTo(zero) >= 0 && y.compareTo(zero) <= 0) {
            BigDecimal xSquared = x.multiply(x);
            BigDecimal ySquared = y.multiply(y);
            BigDecimal rSquared = r.multiply(r);
            BigDecimal sumSquares = xSquared.add(ySquared);

            if (sumSquares.compareTo(rSquared) <= 0) {
                return true;
            }
        }

        if (x.compareTo(zero) <= 0 && y.compareTo(zero) <= 0) {
            BigDecimal rightSide = BigDecimal.valueOf(-2).multiply(x).subtract(r);
            if (y.compareTo(rightSide) >= 0) {
                return true;
            }
        }

        return false;
    }
}
