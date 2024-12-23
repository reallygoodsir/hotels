package org.hotels.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotels.dao.*;
import org.hotels.models.Country;
import org.hotels.models.Hotel;
import org.hotels.models.Room;
import org.hotels.validators.SearchValidator;

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
        } catch (Exception exception) {
            logger.error("Error while getting all countries ", exception);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
            dispatcher.forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            HttpSession session = req.getSession(true);
            List<Country> allCountries = getAllCountries();
            req.setAttribute("allCountries", allCountries);
            SearchValidator validationService = new SearchValidator();

            String countryName = req.getParameter("destination");
            req.setAttribute("countryName", countryName);

            String adults = req.getParameter("adults");
            boolean isAdultCapacityValid = validationService.isAdultCapacityValid(adults);
            int adultCapacity = 0;
            if (isAdultCapacityValid) {
                adultCapacity = Integer.parseInt(req.getParameter("adults"));
                session.setAttribute("adults", adultCapacity);
            } else {
                session.setAttribute("adultCapacityError", "true");
            }

            String children = req.getParameter("children");
            boolean isChildrenCapacityValid = validationService.isChildrenCapacityValid(children);
            int childrenCapacity = 0;
            if (isChildrenCapacityValid) {
                childrenCapacity = Integer.parseInt(req.getParameter("children"));
                session.setAttribute("children", childrenCapacity);
            } else {
                session.setAttribute("childrenCapacityError", "true");
            }

            String checkInString = req.getParameter("check_in");
            boolean isCheckInValid = validationService.isCheckInValid(checkInString);

            if (isCheckInValid) {
                session.setAttribute("checkIn", checkInString);
            } else {
                session.setAttribute("checkInError", "true");
            }

            String checkOutString = req.getParameter("check_out");
            boolean isCheckOutValid = validationService.isCheckOutValid(checkOutString);
            if (isCheckOutValid) {
                session.setAttribute("checkOut", checkOutString);
            } else {
                session.setAttribute("checkOutError", "true");
            }

            boolean isDayDistanceValid = false;
            Date checkInDate = null;
            Date checkOutDate = null;
            if (isCheckInValid && isCheckOutValid) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                checkInDate = dateFormat.parse(checkInString);
                checkOutDate = dateFormat.parse(checkOutString);
                int dayDistance = validationService.isDayDistanceValid(checkInDate, checkOutDate);
                if (dayDistance == -1) {
                    session.setAttribute("dayDistanceTooShort", "true");
                } else if (dayDistance == -2) {
                    session.setAttribute("checkInBeforeCheckOut", "true");
                } else if (dayDistance == -3) {
                    throw new Exception("Error while trying to calculate the day distance");
                } else {
                    isDayDistanceValid = true;
                    session.setAttribute("dayDistance", dayDistance);
                    session.setAttribute("checkInDate", checkInDate);
                    session.setAttribute("checkOutDate", checkOutDate);
                }
            }

            if (isAdultCapacityValid && isChildrenCapacityValid && isCheckInValid && isCheckOutValid && isDayDistanceValid) {
                RoomDAO roomDAO = new RoomDAOImpl();
                HotelDAO hotelDAO = new HotelDAOImpl();
                Map<String, List<Hotel>> hotels = hotelDAO.searchForHotels(countryName, childrenCapacity, adultCapacity);
                if (!hotels.isEmpty()) {
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
                }
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
