package web.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/")
public class ControllerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        boolean hasParams = req.getParameter("x") != null &&
                req.getParameter("y") != null &&
                req.getParameter("r") != null;

        if (hasParams) {
            req.getRequestDispatcher("/check").forward(req, resp);
        } else{
            req.getRequestDispatcher("index.jsp").forward(req, resp);
        }
    }
}
