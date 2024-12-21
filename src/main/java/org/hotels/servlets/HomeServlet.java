package org.hotels.servlets;

import org.hotels.dao.*;
import org.hotels.models.Country;
import org.hotels.models.Hotel;
import org.hotels.models.Room;

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
import java.util.ArrayList;
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
            Date checkIn = new Date();
            Date checkOut = new Date();
            HttpSession session = req.getSession(true);
            if (!checkInString.isEmpty() && !checkOutString.isEmpty() && !countryName.isEmpty()
                    && !childrenCapacityParameter.isEmpty() && !adultCapacityParameter.isEmpty()) {

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                checkIn = dateFormat.parse(checkInString);
                checkOut = dateFormat.parse(checkOutString);

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
                session.invalidate();
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/home.jsp");
                dispatcher.forward(req, resp);
            }

            RoomDAO roomDAO = new RoomDAOImpl();
            HotelDAO hotelDAO = new HotelDAOImpl();
            Map<String, List<Hotel>> hotels = hotelDAO.searchForHotels(countryName, childrenCapacity, adultCapacity);

            List<Room> allRooms = new ArrayList<>();
            for (List<Hotel> listHotel : hotels.values()) {
                for (Hotel hotel : listHotel) {
                    List<Room> rooms = roomDAO.getRoomsForHotel(hotel.getId(), adultCapacity, childrenCapacity);
                    if (!rooms.isEmpty()) {
                        for (Room room : rooms) {
                            room.setHotelId(hotel.getId());
                            allRooms.add(room);
                        }
                    }
                }
            }
            // verify the allRooms list availability in a separate method in room dao
            List<Room> unreservedRooms = roomDAO.getUnreservedRooms(allRooms, checkIn, checkOut);

            for (List<Hotel> hotelList : hotels.values()) {
                for (Hotel hotel : hotelList) {
                    for (Room room : unreservedRooms) {
                        if (hotel.getId() == room.getHotelId()) {
                            List<Room> rooms = hotel.getRooms();
                            if (rooms == null) {
                                hotel.setRooms(new ArrayList<>());
                            }
                            hotel.getRooms().add(room);
                        }
                    }
                }
            }

            for (List<Hotel> hotelList : hotels.values()) {
                hotelList.removeIf(hotel -> hotel.getRooms() == null || hotel.getRooms().isEmpty());
            }
            req.setAttribute("countryName", countryName);
            session.setAttribute("hotels", hotels);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/home.jsp");
            dispatcher.forward(req, resp);
        } catch (Exception exception) {
            System.err.println("Error while in HomeServlet doPost " + exception.getMessage());
            exception.printStackTrace();
        }
    }

    private static List<Country> getAllCountries() {
        CountryDAO countryDAO = new CountryDAOImpl();
        return countryDAO.getAllCountries();
    }
}
