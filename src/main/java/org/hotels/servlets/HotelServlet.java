package org.hotels.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotels.dao.HotelDAO;
import org.hotels.dao.HotelDAOImpl;
import org.hotels.models.Hotel;
import org.hotels.models.Room;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HotelServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(HotelServlet.class);
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            HttpSession session = req.getSession(false);
            if (session == null) {
                throw new Exception("session should be created");
            }

            String hotelId = req.getParameter("hotelId");

            Map<String, List<Hotel>> hotelMap = (Map<String, List<Hotel>>) session.getAttribute("hotels");
            for (List<Hotel> hotels : hotelMap.values()) {
                for (Hotel hotel : hotels) {
                    if(hotel.getId() == Integer.parseInt(hotelId)){
                        List<Room> roomsForHotel = hotel.getRooms();
                        HotelDAO hotelDAO = new HotelDAOImpl();
                        Optional<Hotel> optionalHotel = hotelDAO.getHotel(hotel.getId());
                        if(optionalHotel.isPresent()){
                            hotel = optionalHotel.get();
                        }else{
                            throw new Exception("Hotel Id is supposed to be valid");
                        }
//                        session.setAttribute("hotel", hotel);
//                        session.setAttribute("hotelName", hotel.getName());
//                        session.setAttribute("hotelId", hotel.getId());
//                        session.setAttribute("hotelRooms", roomsForHotel);
                    }
                }
            }

            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/hotel.jsp");
            dispatcher.forward(req, resp);
        } catch (Exception exception) {
            logger.error("Error while processing the chosen hotel ", exception);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
            dispatcher.forward(req, resp);
        }
    }
}
