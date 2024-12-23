package org.hotels.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotels.models.Room;
import org.hotels.models.RoomInfo;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class RoomServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(RoomServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            HttpSession session = req.getSession(false);
            if (session == null) {
                throw new Exception("session should have been created in home servlet");
            }
            List<Room> rooms = (List<Room>) session.getAttribute("hotelRooms");
            int roomId = Integer.parseInt(req.getParameter("roomId"));
            for (Room room : rooms) {
                if (room.getId() == roomId) {
                    RoomInfo roomInfo = room.getRoomInfo();
                    session.setAttribute("roomId", roomId);
                    session.setAttribute("roomType", roomInfo.getRoomType());
                    session.setAttribute("roomDetails", roomInfo.getDetails());
                    session.setAttribute("roomPrice", roomInfo.getPricePerNight());
                    session.setAttribute("roomNumber", room.getRoomNumber());
                    session.setAttribute("roomHasAirConditioning", roomInfo.isHasAirConditioning());
                    break;
                }
            }
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/room.jsp");
            dispatcher.forward(req, resp);
        } catch (Exception exception) {
            logger.error("Error while getting room information with the room id {} ",
                    req.getParameter("roomId"), exception);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
            dispatcher.forward(req, resp);
        }
    }
}
