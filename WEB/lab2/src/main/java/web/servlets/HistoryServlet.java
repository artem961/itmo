package web.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.tools.http.CalcResultsArray;
import web.tools.http.StandartCalcResult;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

@WebServlet("/history")
public class HistoryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CalcResultsArray resultsArray = (CalcResultsArray) req.getSession().getAttribute("results");

        resp.setContentType("application/json");
        if (resultsArray != null) {
            resp.getWriter().write(resultsArray.toJson());
        } else {
            resp.getWriter().write(new CalcResultsArray(new ArrayList<>()).toJson());
        }
    }
}
