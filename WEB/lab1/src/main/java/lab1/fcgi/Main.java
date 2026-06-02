package lab1.fcgi;

import com.fastcgi.FCGIInterface;
import lab1.fcgi.tools.Validator;
import lab1.fcgi.tools.http.ServerException;
import lab1.fcgi.tools.json.CalcResultsArray;
import lab1.fcgi.tools.http.QueryParamsParser;
import lab1.fcgi.tools.http.StatusCode;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static lab1.fcgi.tools.Validator.*;
import static lab1.fcgi.tools.fcgi.ResponseSender.sendError;
import static lab1.fcgi.tools.fcgi.ResponseSender.sendJsonResponse;

public class Main {
    public static void main(String[] args) {

        while (new FCGIInterface().FCGIaccept() >= 0) {
            try {
                String method = FCGIInterface.request.params.getProperty("REQUEST_METHOD");

                if (method.equals("GET")) {
                    String queryString = FCGIInterface.request.params.getProperty("QUERY_STRING");
                    Map<String, List<String>> queryParams = QueryParamsParser.parseQueryParams(queryString);
                    List<String> xParam = queryParams.get("x");
                    List<String> yParam = queryParams.get("y");
                    List<String> rParam = queryParams.get("r");

                    if (rParam == null || yParam == null || xParam == null) {
                        throw new ServerException(StatusCode.BAD_REQUEST, "Введены не все параметры!");
                    }

                    List<BigDecimal> r = rParam.stream()
                            .map(Validator::parseNumber)
                            .collect(Collectors.toList());
                    BigDecimal x = parseNumber(xParam.get(0));
                    BigDecimal y = parseNumber(yParam.get(0));

                    checkRanges(x, y, r);
                    CalcResultsArray resultsArray = new CalcResultsArray(checkHits(x, y, r));
                    sendJsonResponse(StatusCode.OK, resultsArray.toJson());
                }
            } catch (ServerException e) {
                sendError(e);
            } catch (Exception e) {
                sendError(new ServerException(StatusCode.INTERNAL_SERVER_ERROR, e.getMessage()));
            }
        }
    }

    private static void checkRanges(BigDecimal x, BigDecimal y, List<BigDecimal> r) {
        r.forEach(item -> {
            if (!checkRange(item, new BigDecimal(0), BigDecimal.valueOf(Double.MAX_VALUE))){
                throw new ServerException(StatusCode.BAD_REQUEST, "Значение r должно быть больше или равно 0!");
            }
        });

         if (!checkRange(y, new BigDecimal(-5), new BigDecimal(3))){
            throw new ServerException(StatusCode.BAD_REQUEST, "Значение y должно быть в диапазоне -5 ... 3");
        } else if (!checkRange(x, new BigDecimal(-5), new BigDecimal(3))){
            throw new ServerException(StatusCode.BAD_REQUEST, "Значение x должно быть в диапазоне -5 ... 3");
        }
    }
}
