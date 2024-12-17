package org.hotels.dao;

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

public class RoomDAOImpl implements RoomDAO {
    private static final String SELECT_ROOMS_FOR_HOTEL = "select  " +
            "room.room_id as room_id, " +
            "room.room_number as room_number, " +
            "room_info.room_type as room_type, " +
            "room_info.price_per_night as price, " +
            "room.is_available as available," +
            "room_info.adults_capacity as max_adults, " +
            "room_info.children_capacity as max_children, " +
            "room_info.details as room_details " +
            "from room room " +
            "join hotel hotel " +
            "on hotel.hotel_id = room.hotel_id " +
            "join room_info room_info " +
            "on room_info.room_id = room.room_id " +
            "where room.hotel_id = ? " +
            "and room_info.adults_capacity = ? " +
            "and room_info.children_capacity = ?";

    @Override
    public List<Room> getRoomsForHotel(int hotelId, int adultCapacity, int childrenCapacity) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
             PreparedStatement stmtSelectRooms = connection.prepareStatement(SELECT_ROOMS_FOR_HOTEL)) {
            stmtSelectRooms.setInt(1, hotelId);
            stmtSelectRooms.setInt(2, adultCapacity);
            stmtSelectRooms.setInt(3, childrenCapacity);
            ResultSet resultSet = stmtSelectRooms.executeQuery();
            List<Room> rooms = new ArrayList<>();
            while(resultSet.next()){
                int roomAvailable = resultSet.getInt("available");
                if(roomAvailable == 1){
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
                    room.setRoomInfo(roomInfo);

                    rooms.add(room);
                }
            }
            if(!rooms.isEmpty()){
                return rooms;
            }else {
                System.out.println("no rooms for hotel with the id " + hotelId); // replace this with logger.warn
                return Collections.emptyList();
            }
        } catch (Exception exception) {
            System.err.println("Error while getting rooms for the hotel with the id " + hotelId + exception.getMessage());
            exception.printStackTrace();
            return Collections.emptyList();
        }
    }
}
