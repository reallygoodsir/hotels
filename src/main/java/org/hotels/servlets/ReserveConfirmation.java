package org.hotels.servlets;

import org.hotels.dao.CustomerDAO;
import org.hotels.dao.CustomerDAOImpl;
import org.hotels.dao.TransactionDAO;
import org.hotels.dao.TransactionDAOImpl;

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
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String email = req.getParameter("email");
            String name = req.getParameter("name");
            String phoneNumber = req.getParameter("phoneNumber");
            CustomerDAO customerDAO = new CustomerDAOImpl();
            int customerId = customerDAO.createCustomer(email, name, phoneNumber);

            String priceParameter = req.getParameter("roomPrice");
            BigDecimal pricePerNight = new BigDecimal(priceParameter);
            HttpSession session = req.getSession(false);
            if (session == null) {
                throw new Exception("no session in confirm reserve servlet");
            }
            Long days = (Long) session.getAttribute("dayDistance");
            BigDecimal totalPrice = pricePerNight.multiply(new BigDecimal(days));
            req.setAttribute("totalPrice", totalPrice);

            TransactionDAO transactionDAO = new TransactionDAOImpl();
            Date checkIn = (Date) session.getAttribute("checkIn");
            Date checkOut = (Date) session.getAttribute("checkOut");
            int hotelId = Integer.parseInt(req.getParameter("hotelId"));
            int roomId = Integer.parseInt(req.getParameter("hotelId"));
            transactionDAO.executeTransaction(totalPrice, checkIn, checkOut, customerId, hotelId, roomId);


            session.invalidate();
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/reserve-confirmation.jsp");
            dispatcher.forward(req, resp);
        } catch (Exception exception) {
            System.err.println("Error while trying to initialize and confirm the reservation" + exception.getMessage());
            exception.printStackTrace();
        }
    }
}
