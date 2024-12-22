package org.hotels.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotels.dao.*;
import org.hotels.models.Country;
import org.hotels.models.Hotel;
import org.hotels.models.Room;
import org.hotels.validators.SearchValidation;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class HomeServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(HomeServlet.class);
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Country> allCountries = getAllCountries();
            req.setAttribute("allCountries", allCountries);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/home.jsp");
            dispatcher.forward(req, resp);
        }catch (Exception exception){
            logger.error("Error while getting all countries ", exception);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
            dispatcher.forward(req, resp);
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Country> allCountries = getAllCountries();
            req.setAttribute("allCountries", allCountries);

            HttpSession session = req.getSession(true);
            SearchValidation validationService = new SearchValidation();
            String countryName = req.getParameter("destination");
            String children = req.getParameter("children");
            int childrenCapacity = validationService.validateChildrenCapacity(children);
            String adults = req.getParameter("adults");
            int adultCapacity = validationService.validateAdultCapacity(adults);
            if (childrenCapacity < 0) {
                session.setAttribute("childrenCapacityError", "f");
            } else {
                session.setAttribute("children", Integer.parseInt(req.getParameter("children")));
            }
            if (adultCapacity < 1) {
                session.setAttribute("adultCapacityError", "true");
            } else {
                session.setAttribute("adults", Integer.parseInt(req.getParameter("adults")));
            }
            String checkInString = req.getParameter("check_in");
            String checkOutString = req.getParameter("check_out");

            boolean isValidCheckIn = validationService.validateCheckIn(checkInString);
            boolean isValidCheckOut = validationService.validateCheckOut(checkOutString);
            Date checkInDate = null;
            Date checkOutDate = null;
            if (isValidCheckIn && isValidCheckOut) {
                session.setAttribute("checkIn", checkInString);
                session.setAttribute("checkOut", checkOutString);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                checkInDate = dateFormat.parse(checkInString);
                checkOutDate = dateFormat.parse(checkOutString);
                int dayDistance = validationService.validateAndCalculateDayDistance(checkInDate, checkOutDate);
                if (dayDistance == -1) {
                    session.setAttribute("dayDistanceTooShort", "true");
                } else if (dayDistance == -2) {
                    session.setAttribute("checkInBeforeCheckOut", "true");
                } else {
                    session.setAttribute("dayDistance", dayDistance);
                    session.setAttribute("checkInDate", checkInDate);
                    session.setAttribute("checkOutDate", checkOutDate);
                }
            } else {
                if (!isValidCheckIn) {
                    session.setAttribute("checkInError", "true");
                } else {
                    session.setAttribute("checkIn", checkInString);
                }
                if (!isValidCheckOut) {
                    session.setAttribute("checkOutError", "true");
                } else {
                    session.setAttribute("checkOut", checkOutString);
                }
            }
            if (isValidCheckIn && isValidCheckOut) {
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
                List<Room> unreservedRooms = roomDAO.getUnreservedRooms(allRooms, checkInDate, checkOutDate);

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
            }
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/home.jsp");
            dispatcher.forward(req, resp);
        } catch (Exception exception) {
            logger.error("Error while processing the user search ", exception);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
            dispatcher.forward(req, resp);
        }
    }

    private static List<Country> getAllCountries() {
        CountryDAO countryDAO = new CountryDAOImpl();
        return countryDAO.getAllCountries();
    }
}
