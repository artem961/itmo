package web.servlets;


import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.java.Log;
import web.models.Point;
import web.tools.http.CalcResultsArray;
import web.tools.http.ServerException;
import web.tools.http.StandartCalcResult;
import web.tools.http.StatusCode;
import web.tools.Validator;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Log
@WebServlet("/check")
public class AreaCheckServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            BigDecimal x = Validator.parseNumber(req.getParameter("x"));
            BigDecimal y = Validator.parseNumber(req.getParameter("y"));
            List<BigDecimal> r = Arrays.stream(req.getParameterValues("r"))
                    .map(Validator::parseNumber)
                    .collect(Collectors.toList());

            Point point = new Point(x, y);
            List<StandartCalcResult> results = checkHits(point, r);

            if (req.getSession().getAttribute("results") != null) {
                CalcResultsArray resultsArray = (CalcResultsArray) req.getSession().getAttribute("results");
                List<StandartCalcResult> reverseResults = new ArrayList<>(results);
                Collections.reverse(reverseResults);
                resultsArray.results().addAll(0, reverseResults);
                req.getSession().setAttribute("results", resultsArray);
            } else{
                req.getSession().setAttribute("results", new CalcResultsArray(results));
            }

            CalcResultsArray answer = new CalcResultsArray(results);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(answer.toJson());
            resp.setStatus(StatusCode.OK.getStatusCode());
        } catch (ServerException e){
            sendError(req, resp, e);
        } catch (Exception e) {
            sendError(req, resp, new ServerException(StatusCode.INTERNAL_SERVER_ERROR, e.getMessage()));
        }
    }

    protected void sendError(HttpServletRequest req, HttpServletResponse resp, ServerException e) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(e.getStatusCode().getStatusCode());
        resp.getWriter().print(e.toJson());
    }

    private List<StandartCalcResult> checkHits(Point point, List<BigDecimal> r){
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
                    String.valueOf(endTime - startTime),
                    String.valueOf(LocalTime.now().withNano(0)));
            results.add(standartResult);
        });
        return results;
    }
}
