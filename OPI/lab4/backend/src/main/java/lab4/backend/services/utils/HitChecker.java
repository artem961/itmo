package lab4.backend.services.utils;

import lab4.backend.dto.ResultDTO;
import lab4.backend.dto.DotDTO;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class HitChecker {
    public static ResultDTO checkHit(DotDTO dotDTO){
        Long startTime = System.nanoTime();
        Boolean result = check(dotDTO.getX(), dotDTO.getY(), dotDTO.getR());
        Long endTime = System.nanoTime();

        return ResultDTO.builder()
                .x(dotDTO.getX())
                .y(dotDTO.getY())
                .r(dotDTO.getR())
                .result(result)
                .time(endTime - startTime)
                .currentTime(OffsetDateTime.now(ZoneOffset.UTC).withNano(0))
                .build();
    }

    private static Boolean check(BigDecimal x, BigDecimal y, BigDecimal r) {
        BigDecimal zero = BigDecimal.ZERO;
        BigDecimal halfR = r.divide(BigDecimal.valueOf(2), MathContext.DECIMAL128);

        if (x.compareTo(zero) <= 0 && y.compareTo(zero) <= 0 &&
                x.compareTo(r.negate()) >= 0 && y.compareTo(r.negate()) >= 0) {
            return true;
        }

        if (x.compareTo(zero) <= 0 && y.compareTo(zero) >= 0) {
            BigDecimal xSquared = x.multiply(x);
            BigDecimal ySquared = y.multiply(y);
            BigDecimal rSquared = halfR.multiply(halfR);
            BigDecimal sumSquares = xSquared.add(ySquared);

            if (sumSquares.compareTo(rSquared) <= 0) {
                return true;
            }
        }

        if (x.compareTo(zero) >= 0 && y.compareTo(zero) >= 0) {
            BigDecimal func = x.negate().add(r);
            if (y.compareTo(func) <= 0) {
                return true;
            }
        }

        return false;
    }


}
