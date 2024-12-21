package org.hotels.servlets;

import org.hotels.models.Room;
import org.hotels.models.RoomInfo;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class RoomServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            HttpSession session = req.getSession(false);
            if (session == null) {
                throw new Exception("session should be created");
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
                    System.out.println("\n\n\n\n\n" + roomInfo.isHasAirConditioning());
                    break;
                }
            }
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/room.jsp");
            dispatcher.forward(req, resp);
        } catch (Exception exception) {
            System.err.println("Error in room servlet" + exception.getMessage());
            exception.printStackTrace();
        }
    }
}
