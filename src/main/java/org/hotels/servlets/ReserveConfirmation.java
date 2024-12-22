package org.hotels.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotels.dao.TransactionDAO;
import org.hotels.dao.TransactionDAOImpl;
import org.hotels.models.Customer;
import org.hotels.models.Transaction;
import org.hotels.services.EmailService;
import org.hotels.validators.CustomerValidator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

public class ReserveConfirmation extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(ReserveConfirmation.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            HttpSession session = req.getSession(false);
            if (session == null) {
                throw new Exception("no session in confirm reserve servlet");
            }
            String userEmailConfirmationCode = req.getParameter("emailConfirmationCode");
            String emailConfirmationCode = (String) session.getAttribute("emailConfirmationCode");
            CustomerValidator customerValidator = new CustomerValidator();
            if (!customerValidator.isValidEmailCode(userEmailConfirmationCode, emailConfirmationCode)) {
                req.setAttribute("invalidEmailCode", "true");
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/reserve.jsp");
                dispatcher.forward(req, resp);
                return;
            }
            String email = (String) session.getAttribute("email");
            String name = (String) session.getAttribute("name");
            String phoneNumber = (String) session.getAttribute("phoneNumber");

            BigDecimal pricePerNight = (BigDecimal) session.getAttribute("roomPrice");
            Integer days = (Integer) session.getAttribute("dayDistance");
            BigDecimal totalPrice = pricePerNight.multiply(new BigDecimal(days));
            req.setAttribute("totalPrice", totalPrice);
            Date checkIn = (Date) session.getAttribute("checkInDate");
            Date checkOut = (Date) session.getAttribute("checkOutDate");
            int hotelId = (int) session.getAttribute("hotelId");
            int roomId = (int) session.getAttribute("roomId");

            Customer customer = new Customer(name, email, phoneNumber);
            Transaction transaction = new Transaction(roomId, hotelId, totalPrice, checkIn, checkOut);
            TransactionDAO transactionDAO = new TransactionDAOImpl();
            String confirmationNumber = transactionDAO.executeTransaction(customer, transaction);
            req.setAttribute("confirmationNumber", confirmationNumber);
            session.invalidate();
            EmailService emailService = new EmailService();
            boolean hotelReservations = emailService.send("Hotel Reservations", "Your reservation number is " + confirmationNumber, email);
            if(!hotelReservations){
                throw new Exception("Error while sending the message to user email");
            }
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/reserve-confirmation.jsp");
            dispatcher.forward(req, resp);
        } catch (Exception exception) {
            logger.error("Error while trying to initialize and confirm the reservation", exception);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
            dispatcher.forward(req, resp);
        }
    }
}
