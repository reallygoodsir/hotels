package org.hotels.servlets;

import org.hotels.dao.CountryDAO;
import org.hotels.dao.CountryDAOImpl;
import org.hotels.dao.HotelDAO;
import org.hotels.dao.HotelDAOImpl;
import org.hotels.models.Country;
import org.hotels.models.Hotel;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class HomeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Country> allCountries = getAllCountries();
        req.setAttribute("allCountries", allCountries);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/home.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            List<Country> allCountries = getAllCountries();
            req.setAttribute("allCountries", allCountries);

            String countryName = req.getParameter("destination");
            String childrenCapacityParameter = req.getParameter("children");
            String adultCapacityParameter = req.getParameter("adults");
            int childrenCapacity = Integer.parseInt(req.getParameter("children"));
            int adultCapacity = Integer.parseInt(req.getParameter("adults"));

            String checkInString = req.getParameter("check_in");
            String checkOutString = req.getParameter("check_out");
            if (!checkInString.isEmpty() && !checkOutString.isEmpty() && !countryName.isEmpty()
                    && !childrenCapacityParameter.isEmpty() && !adultCapacityParameter.isEmpty()) {

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date checkIn = dateFormat.parse(checkInString);
                Date checkOut = dateFormat.parse(checkOutString);
                HttpSession session = req.getSession(true);

                LocalDate checkInLocalDate = checkIn.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
                LocalDate checkOutLocalDate = checkOut.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();

                long dayDistance = ChronoUnit.DAYS.between(checkInLocalDate, checkOutLocalDate);
                if (dayDistance < 1) {
                    throw new RuntimeException("Stay date can't be under a day");
                }
                session.setAttribute("dayDistance", dayDistance);
                session.setAttribute("checkIn", checkIn);
                session.setAttribute("checkOut", checkOut);
                session.setAttribute("children", Integer.parseInt(req.getParameter("children")));
                session.setAttribute("adults", Integer.parseInt(req.getParameter("adults")));
            } else {
                // code telling the person that not everything has been entered
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/home.jsp");
                dispatcher.forward(req, resp);
            }

            HotelDAO hotelDAO = new HotelDAOImpl();
            Map<String, List<Hotel>> hotels = hotelDAO.searchForHotels(countryName, childrenCapacity, adultCapacity);
            // check
            req.setAttribute("countryName", countryName);
            req.setAttribute("hotels", hotels);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/home.jsp");
            dispatcher.forward(req, resp);
        } catch (Exception exception) {
            System.err.println("Error while in HomeServlet doPost " + exception.getMessage());
        }
    }

    private static List<Country> getAllCountries() {
        CountryDAO countryDAO = new CountryDAOImpl();
        return countryDAO.getAllCountries();
    }
}
