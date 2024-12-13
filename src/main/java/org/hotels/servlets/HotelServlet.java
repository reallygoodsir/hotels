package org.hotels.servlets;

import org.hotels.dao.HotelDAO;
import org.hotels.dao.HotelDAOImpl;
import org.hotels.dao.RoomDAO;
import org.hotels.dao.RoomDAOImpl;
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
import java.util.Optional;

public class HotelServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            HotelDAO hotelDAO = new HotelDAOImpl();
            String hotelId = req.getParameter("hotelId");
            Optional<Hotel> optionalHotel = hotelDAO.getHotel(Integer.parseInt(hotelId));
            if (optionalHotel.isPresent()) {
                Hotel hotel = optionalHotel.get();
                req.setAttribute("hotel", hotel);
            }
            HttpSession session = req.getSession(false);
            if (session == null) {
                throw new Exception("session should be created");
            }
            int adultCapacity = (int) session.getAttribute("adults");
            int childrenCapacity = (int) session.getAttribute("children");
            RoomDAO roomDAO = new RoomDAOImpl();
            List<Room> roomsForHotel = roomDAO.getRoomsForHotel(Integer.parseInt(hotelId), adultCapacity, childrenCapacity);
            req.setAttribute("hotelRooms", roomsForHotel);

            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/hotel.jsp");
            dispatcher.forward(req, resp);
        }catch (Exception exception){
            System.err.println("Error in hotel servlet" + exception.getMessage());
        }
    }
}
