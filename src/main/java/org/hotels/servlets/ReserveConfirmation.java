package org.hotels.servlets;

import org.hotels.dao.TransactionDAO;
import org.hotels.dao.TransactionDAOImpl;
import org.hotels.models.Customer;
import org.hotels.models.Transaction;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Date;

public class ReserveConfirmation extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            HttpSession session = req.getSession(false);
            if (session == null) {
                throw new Exception("no session in confirm reserve servlet");
            }
            String email = req.getParameter("email");
            String name = req.getParameter("name");
            String phoneNumber = req.getParameter("phoneNumber");

            BigDecimal pricePerNight = (BigDecimal) session.getAttribute("roomPrice");
            Long days = (Long) session.getAttribute("dayDistance");
            BigDecimal totalPrice = pricePerNight.multiply(new BigDecimal(days));
            req.setAttribute("totalPrice", totalPrice);
            Date checkIn = (Date) session.getAttribute("checkIn");
            Date checkOut = (Date) session.getAttribute("checkOut");
            int hotelId = (int) session.getAttribute("hotelId");
            int roomId = (int) session.getAttribute("roomId");

            Customer customer = new Customer(name, email, phoneNumber);
            Transaction transaction = new Transaction(roomId, hotelId, totalPrice, checkIn, checkOut);
            TransactionDAO transactionDAO = new TransactionDAOImpl();
            transactionDAO.executeTransaction(customer, transaction);

            session.invalidate();
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/reserve-confirmation.jsp");
            dispatcher.forward(req, resp);
        } catch (Exception exception) {
            System.err.println("Error while trying to initialize and confirm the reservation" + exception.getMessage());
            exception.printStackTrace();
        }
    }
}
