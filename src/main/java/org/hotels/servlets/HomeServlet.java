package org.hotels.servlets;

import org.hotels.dao.*;
import org.hotels.models.City;
import org.hotels.models.Country;
import org.hotels.models.Hotel;
import org.hotels.models.HotelAddress;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            List<Country> allCountries = getAllCountries();
            req.setAttribute("allCountries", allCountries);

            HotelDAO hotelDAO = new HotelDAOImpl();
            // check_in, check_out, country name, children and adult amount
            String countryName = req.getParameter("destination");
            String checkInString = req.getParameter("check_in");
            String checkOutString = req.getParameter("check_out");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date checkIn = dateFormat.parse(checkInString);
            Date checkOut = dateFormat.parse(checkOutString);
            int childrenCapacity = Integer.parseInt(req.getParameter("children"));
            int adultCapacity = Integer.parseInt(req.getParameter("adults"));
            List<Hotel> hotels = hotelDAO.searchForHotels(checkIn, checkOut, countryName, childrenCapacity, adultCapacity);
            CityDAO cityDAO = new CityDAOImpl();
            List<Country> countries = new ArrayList<>();
            for (Hotel hotelName : hotels) {
                HotelAddress hotelAddress = hotelName.getHotelAddress();
                String cityName = cityDAO.getById(hotelAddress.getCityId());

                Country country = new Country();
                country.setName(countryName);
                List<City> cities = new ArrayList<>();
                City city = new City();
                city.setName(cityName);
                cities.add(city);
                country.setCities(cities);
                countries.add(country);
            }

            Map<List<Hotel>, List<Country>> hotelsMap = new HashMap<>();
            hotelsMap.put(hotels, countries);
            req.setAttribute("hotels", hotelsMap);

            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/home.jsp");
            dispatcher.forward(req, resp);
        }catch (Exception exception){
            System.err.println("Error while in HomeServlet doPost " + exception.getMessage());
        }
    }

    private static List<Country> getAllCountries(){
        CountryDAO countryDAO = new CountryDAOImpl();
        List<Country> allCountries = countryDAO.getAllCountries();
        return allCountries;
    }
}
