package org.hotels.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotels.models.Room;
import org.hotels.models.RoomInfo;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RoomDAOImpl implements RoomDAO {
    private static final Logger logger = LogManager.getLogger(RoomDAOImpl.class);
    private static final String SELECT_ROOMS_FOR_HOTEL = "select  " +
            "room.room_id as room_id, " +
            "room.room_number as room_number, " +
            "room_info.room_type as room_type, " +
            "room_info.price_per_night as price, " +
            "room.is_available as available," +
            "room_info.adults_capacity as max_adults, " +
            "room_info.children_capacity as max_children, " +
            "room_info.has_air_conditioning as air_conditioning, " +
            "room_info.details as room_details " +
            "from room room " +
            "join hotel hotel " +
            "on hotel.hotel_id = room.hotel_id " +
            "join room_info room_info " +
            "on room_info.room_id = room.room_id " +
            "where room.hotel_id = ? " +
            "and room_info.adults_capacity = ? " +
            "and room_info.children_capacity = ?";

    private static final String VERIFY_ROOMS_AVAILABILITY = "SELECT distinct room_id " +
            "FROM transaction " +
            "WHERE room_id = ? " +
            "AND (? <= check_out AND ? >= check_in)";

    @Override
    public List<Room> getRoomsForHotel(int hotelId, int adultCapacity, int childrenCapacity) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
             PreparedStatement stmtSelectRooms = connection.prepareStatement(SELECT_ROOMS_FOR_HOTEL)) {
            stmtSelectRooms.setInt(1, hotelId);
            stmtSelectRooms.setInt(2, adultCapacity);
            stmtSelectRooms.setInt(3, childrenCapacity);
            ResultSet resultSet = stmtSelectRooms.executeQuery();
            List<Room> rooms = new ArrayList<>();
            while (resultSet.next()) {
                int roomAvailable = resultSet.getInt("available");
                if (roomAvailable == 1) {
                    Room room = new Room();
                    room.setRoomNumber(resultSet.getString("room_number"));
                    room.setId(resultSet.getInt("room_id"));
                    room.setHotelId(hotelId);

                    RoomInfo roomInfo = new RoomInfo();
                    roomInfo.setDetails(resultSet.getString("room_details"));
                    roomInfo.setAdultsCapacity(resultSet.getInt("max_adults"));
                    roomInfo.setChildrenCapacity(resultSet.getInt("max_children"));
                    roomInfo.setRoomType(resultSet.getString("room_type"));
                    roomInfo.setPricePerNight(BigDecimal.valueOf(resultSet.getInt("price")));
                    roomInfo.setHasAirConditioning(resultSet.getBoolean("air_conditioning"));
                    room.setRoomInfo(roomInfo);

                    rooms.add(room);
                }
            }
            if (!rooms.isEmpty()) {
                return rooms;
            } else {
                return Collections.emptyList();
            }
        } catch (Exception exception) {
            logger.error("Error while getting rooms for the hotel with the id {}", hotelId, exception);
            return Collections.emptyList();
        }
    }

    @Override
    public List<Room> getUnreservedRooms(List<Room> rooms, java.util.Date checkIn, java.util.Date checkOut) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD)) {
            List<Room> availableRooms = new ArrayList<>();
            for (Room room : rooms) {
                try (PreparedStatement stmtVerifyRooms = connection.prepareStatement(VERIFY_ROOMS_AVAILABILITY)) {
                    stmtVerifyRooms.setInt(1, room.getId());
                    java.sql.Date sqlCheckIn = new java.sql.Date(checkIn.getTime());
                    java.sql.Date sqlCheckOut = new java.sql.Date(checkOut.getTime());
                    stmtVerifyRooms.setDate(2, sqlCheckIn);
                    stmtVerifyRooms.setDate(3, sqlCheckOut);

                    ResultSet resultSet = stmtVerifyRooms.executeQuery();
                    if (!resultSet.next()) {
                        availableRooms.add(room);
                    }
                }
            }
            List<Integer> availableRoomsIds = availableRooms.stream().map(Room::getId).collect(Collectors.toList());
            logger.info("Available rooms ids: {} ", availableRoomsIds);
            return availableRooms;
        } catch (Exception exception) {
            logger.error("Error while getting all the unreserved rooms: ", exception);
            return Collections.emptyList();
        }
    }
}
