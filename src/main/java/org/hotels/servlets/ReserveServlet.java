package org.hotels.servlets;

import org.hotels.services.EmailService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Random;

public class ReserveServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/reserve.jsp");
            dispatcher.forward(req, resp);
        } catch (Exception exception) {
            System.err.println("Error in reserve Servlet" + exception.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            HttpSession session = req.getSession(false);
            if (session == null) {
                throw new Exception("no session in confirm reserve servlet");
            }
            String email = req.getParameter("email");
            String phoneNumber = req.getParameter("phoneNumber");
            String name = req.getParameter("name");
            StringBuilder emailConfirmationCode = new StringBuilder();
            Random random = new Random();
            for (int i = 0; i < 4; i++) {
                int digit = random.nextInt(10);
                emailConfirmationCode.append(digit);
            }
            System.out.println("Code: " + emailConfirmationCode);
            // send to email
            session.setAttribute("emailConfirmationCode", emailConfirmationCode.toString());
            session.setAttribute("email", email);
            session.setAttribute("phoneNumber", phoneNumber);
            session.setAttribute("name", name);
            session.setAttribute("emailConfirmed", "false");

            EmailService emailService = new EmailService();
            emailService.send("Email confirmation code", "Your email confirmation code is " + emailConfirmationCode, email);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/reserve.jsp");
            dispatcher.forward(req, resp);
        } catch (Exception exception) {
            System.err.println("Error in reserve confirmation doGet" + exception.getMessage());
        }
    }
}
