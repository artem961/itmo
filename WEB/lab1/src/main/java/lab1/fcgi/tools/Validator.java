package lab1.fcgi.tools;

import lab1.fcgi.tools.http.ServerException;
import lab1.fcgi.tools.http.StatusCode;
import lab1.fcgi.tools.json.StandartCalcResult;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Validator {
    public static boolean checkHit(BigDecimal x, BigDecimal y, BigDecimal r) {
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

    public static List<StandartCalcResult> checkHits(BigDecimal x, BigDecimal y, List<BigDecimal> r){
        List<StandartCalcResult> results = new ArrayList<>();


        r.forEach((rad) -> {
            Long startTime = System.nanoTime();
            boolean result = checkHit(x, y, rad);
            Long endTime = System.nanoTime();

            StandartCalcResult standartResult = new StandartCalcResult(
                    x,
                    y,
                    rad,
                    result,
                    String.valueOf(endTime - startTime),
                    String.valueOf(LocalTime.now().withNano(0)));
            results.add(standartResult);
        });
        return results;
    }

    public static boolean checkRange(BigDecimal value, BigDecimal min, BigDecimal max) {
        return value.compareTo(min) >= 0 && value.compareTo(max) <= 0;
    }

    public static BigDecimal parseNumber(String number) {
        try {
            return new BigDecimal(number);
        } catch (NumberFormatException e) {
            throw new ServerException(StatusCode.BAD_REQUEST, "Значение " + number + " не является числом!");
        } catch (NullPointerException e){
            throw new ServerException(StatusCode.BAD_REQUEST, "Ожидались параметры x, y, r");
        }
    }
}
