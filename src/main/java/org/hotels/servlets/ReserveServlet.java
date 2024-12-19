package org.hotels.servlets;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ReserveServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/reserve.jsp");
            dispatcher.forward(req, resp);
        } catch (Exception exception) {
            System.err.println("Error in reserve Servlet" + exception.getMessage());
        }
    }
}
