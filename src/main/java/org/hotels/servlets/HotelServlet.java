package org.hotels.servlets;

import org.hotels.dao.HotelDAO;
import org.hotels.dao.HotelDAOImpl;
import org.hotels.dao.RoomDAO;
import org.hotels.dao.RoomDAOImpl;
import org.hotels.models.Hotel;
import org.hotels.models.Room;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HotelServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            HttpSession session = req.getSession(false);
            if (session == null) {
                throw new Exception("session should be created");
            }

            String hotelId = req.getParameter("hotelId");
            int adultCapacity = (int) session.getAttribute("adults");
            int childrenCapacity = (int) session.getAttribute("children");

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
                        session.setAttribute("hotel", hotel);
                        session.setAttribute("hotelName", hotel.getName());
                        session.setAttribute("hotelId", hotel.getId());
                        session.setAttribute("hotelRooms", roomsForHotel);
                    }
                }
            }

            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/hotel.jsp");
            dispatcher.forward(req, resp);
        } catch (Exception exception) {
            exception.printStackTrace();
            System.err.println("Error in hotel servlet" + exception.getMessage());
        }
    }
}
