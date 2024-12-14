package org.hotels.dao;

import org.hotels.models.*;

import java.sql.*;
import java.util.List;

public class HotelDAOImpl implements HotelDAO {
    private static final String INSERT_HOTEL = "insert into hotel " +
            "(name) " +
            "values(?)";

    private static final String INSERT_HOTEL_INFO = "insert into hotel_info " +
            "(hotel_id, phone_number, has_parking, has_wifi, has_swimming_pool, details) " +
            "values(?, ?, ?, ?, ?, ?)";

    private static final String INSERT_HOTEL_ADDRESS = "insert into hotel_address  " +
            "(hotel_id, country_id, city_id, street_id) " +
            "values(?, ?, ?, ?)";

    private static final String INSERT_ROOM = "insert into room  " +
            "(hotel_id, room_number, is_available) " +
            "values(?, ?, ?)";

    private static final String INSERT_ROOM_INFO = "insert into room_info  " +
            "(room_id, room_type, price_per_night, adults_capacity, children_capacity, has_air_conditioning, details) " +
            "values(?, ?, ?, ?, ?, ?, ?)";

    @Override
    public void create(Hotel hotel) {
        try (
                Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD)
        ) {
            connection.setAutoCommit(false);

            int hotelId;
            try (PreparedStatement stmtCreateHotel =
                         connection.prepareStatement(INSERT_HOTEL, Statement.RETURN_GENERATED_KEYS)) {
                stmtCreateHotel.setString(1, hotel.getName());
                stmtCreateHotel.executeUpdate();
                try (ResultSet generatedKeys = stmtCreateHotel.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        hotelId = generatedKeys.getInt(1);
                    } else {
                        connection.rollback();
                        throw new Exception("Couldn't get generated keys");
                    }
                }
            }
            HotelInfo hotelInfo = hotel.getHotelInfo();
            try (PreparedStatement stmtCreateHotelInfo =
                         connection.prepareStatement(INSERT_HOTEL_INFO, Statement.RETURN_GENERATED_KEYS)) {
                stmtCreateHotelInfo.setInt(1, hotelId);
                stmtCreateHotelInfo.setString(2, hotelInfo.getPhoneNumber());
                stmtCreateHotelInfo.setBoolean(3, hotelInfo.isHasParking());
                stmtCreateHotelInfo.setBoolean(4, hotelInfo.isHasWifi());
                stmtCreateHotelInfo.setBoolean(5, hotelInfo.isHasSwimmingPool());
                stmtCreateHotelInfo.setString(6, hotelInfo.getDetails());
                stmtCreateHotelInfo.executeUpdate();
                try (ResultSet generatedKeys = stmtCreateHotelInfo.getGeneratedKeys()) {
                    if (!generatedKeys.next()) {
                        connection.rollback();
                        throw new Exception("Couldn't get generated keys");
                    }
                }
            }

            HotelAddress hotelAddress = hotel.getHotelAddress();
            try (PreparedStatement stmtCreateHotelAddress =
                         connection.prepareStatement(INSERT_HOTEL_ADDRESS, Statement.RETURN_GENERATED_KEYS)) {
                stmtCreateHotelAddress.setInt(1, hotelId);
                stmtCreateHotelAddress.setInt(2, hotelAddress.getCountryId());
                stmtCreateHotelAddress.setInt(3, hotelAddress.getCityId());
                stmtCreateHotelAddress.setInt(4, hotelAddress.getStreetId());
                stmtCreateHotelAddress.executeUpdate();
                try (ResultSet generatedKeys = stmtCreateHotelAddress.getGeneratedKeys()) {
                    if (!generatedKeys.next()) {
                        connection.rollback();
                        throw new Exception("Country/city/street doesn't exist (couldn't create hotelAddress)");
                    }
                }
            }

            List<Room> rooms = hotel.getRooms();
            for (Room room : rooms) {
                int roomId;
                try (PreparedStatement stmtCreateRoom =
                             connection.prepareStatement(INSERT_ROOM, Statement.RETURN_GENERATED_KEYS)) {
                    stmtCreateRoom.setInt(1, hotelId);
                    stmtCreateRoom.setString(2, room.getRoomNumber());
                    stmtCreateRoom.setBoolean(3, room.isAvailable());
                    stmtCreateRoom.executeUpdate();
                    try (ResultSet generatedKeys = stmtCreateRoom.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            roomId = generatedKeys.getInt(1);
                        } else {
                            connection.rollback();
                            throw new Exception("Couldn't get generated keys");
                        }
                    }
                }

                RoomInfo roomInfo = room.getRoomInfo();
                try (PreparedStatement stmtCreateRoomInfo =
                             connection.prepareStatement(INSERT_ROOM_INFO, Statement.RETURN_GENERATED_KEYS)) {
                    stmtCreateRoomInfo.setInt(1, roomId);
                    stmtCreateRoomInfo.setString(2, roomInfo.getRoomType());
                    stmtCreateRoomInfo.setBigDecimal(3, roomInfo.getPricePerNight());
                    stmtCreateRoomInfo.setInt(4, roomInfo.getAdultsCapacity());
                    stmtCreateRoomInfo.setInt(5, roomInfo.getChildrenCapacity());
                    stmtCreateRoomInfo.setBoolean(6, roomInfo.isHasAirConditioning());
                    stmtCreateRoomInfo.setString(7, roomInfo.getDetails());
                    stmtCreateRoomInfo.executeUpdate();
                    try (ResultSet generatedKeys = stmtCreateRoomInfo.getGeneratedKeys()) {
                        if (!generatedKeys.next()) {
                            connection.rollback();
                            throw new Exception("Couldn't get generated keys");
                        }
                    }
                }
            }

            connection.commit();
        } catch (Exception exception) {
            System.err.println("Error while creating/updating hotel" + exception.getMessage());
        }
    }
}
