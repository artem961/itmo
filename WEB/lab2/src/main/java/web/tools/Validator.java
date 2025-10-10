package web.tools;

import web.tools.http.ServerException;
import web.tools.http.StatusCode;

import java.math.BigDecimal;

public class Validator {
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
