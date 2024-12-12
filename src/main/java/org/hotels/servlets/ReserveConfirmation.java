package org.hotels.servlets;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ReserveConfirmation extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        System.out.println("ReserveConfirmation doGet");
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/reserve-confirmation.jsp");
        dispatcher.forward(req, resp);
    }
}
