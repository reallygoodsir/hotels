package org.hotels.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotels.services.EmailService;
import org.hotels.validators.CustomerValidator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Random;

public class ReserveServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(ReserveServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/reserve.jsp");
            dispatcher.forward(req, resp);
        } catch (Exception exception) {
            logger.error("Error while redirecting to reserve jsp ", exception);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
            dispatcher.forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            HttpSession session = req.getSession(false);
            if (session == null) {
                throw new Exception("session should have been created in home servlet");
            }
            String email = req.getParameter("email");
            String phoneNumber = req.getParameter("phoneNumber");
            String name = req.getParameter("name");

            CustomerValidator customerValidator = new CustomerValidator();
            boolean isValidEmail = customerValidator.isEmailValid(email);
            boolean isValidPhoneNumber = customerValidator.isPhoneNumberValid(phoneNumber);

            session.setAttribute("email", email);
            session.setAttribute("phoneNumber", phoneNumber);
            session.setAttribute("name", name);

            if (!isValidEmail) {
                session.setAttribute("emailError", "true");
            }
            if (!isValidPhoneNumber) {
                session.setAttribute("phoneNumberError", "true");
            }

            if (isValidEmail && isValidPhoneNumber) {
                StringBuilder emailConfirmationCode = new StringBuilder();
                Random random = new Random();
                for (int i = 0; i < 4; i++) {
                    int digit = random.nextInt(10);
                    emailConfirmationCode.append(digit);
                }
                logger.info("Code: {}", emailConfirmationCode);
                session.setAttribute("emailConfirmationCode", emailConfirmationCode.toString());
                session.setAttribute("emailConfirmed", "false");

                EmailService emailService = new EmailService();
                emailService.send("Email confirmation code", "Your email confirmation code is " + emailConfirmationCode, email);
            }
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/reserve.jsp");
            dispatcher.forward(req, resp);
        } catch (Exception exception) {
            logger.error("Error while processing the user info ", exception);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
            dispatcher.forward(req, resp);
        }
    }
}
