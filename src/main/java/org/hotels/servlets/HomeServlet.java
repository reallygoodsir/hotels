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
import java.io.IOException;
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

            HotelDAO hotelDAO = new HotelDAOImpl();
            // check_in, check_out, country name, children and adult amount
            String countryName = req.getParameter("destination");
//            String checkInString = req.getParameter("check_in");
//            String checkOutString = req.getParameter("check_out");
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            Date checkIn = dateFormat.parse(checkInString);
//            Date checkOut = dateFormat.parse(checkOutString);
            int childrenCapacity = Integer.parseInt(req.getParameter("children"));
            int adultCapacity = Integer.parseInt(req.getParameter("adults"));
            Map<String, List<Hotel>> hotels = hotelDAO.searchForHotels(countryName, childrenCapacity, adultCapacity);
            req.setAttribute("countryName", countryName);
            req.setAttribute("hotels", hotels);
            System.out.println("entry");
            for (Map.Entry<String, List<Hotel>> entry : hotels.entrySet()) {
                String key = entry.getKey();
                List<Hotel> hotelList = entry.getValue();

                System.out.println("Key: " + key);
                System.out.println("Hotels:");
                for (Hotel hotel : hotelList) {
                    System.out.println("    " + hotel); // Assuming Hotel has a meaningful toString method
                }
            }
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
