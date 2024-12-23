package org.hotels.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotels.models.*;

import java.sql.*;
import java.util.*;

public class HotelDAOImpl implements HotelDAO {
    private static final Logger logger = LogManager.getLogger(HotelDAOImpl.class);
    private static final String SELECT_HOTEL = "select " +
            "hotel.name as hotel_name, " +
            "hotel_info.details as details, " +
            "hotel_info.has_parking as parking, " +
            "hotel_info.has_wifi as wifi, " +
            "hotel_info.has_swimming_pool as swimming_pool " +
            "from hotel hotel " +
            "join hotel_info hotel_info " +
            "on hotel.hotel_id = hotel_info.hotel_id " +
            "where hotel.hotel_id = ?";
    private static final String SEARCH_FOR_HOTELS =
            "select distinct hotel.hotel_id as hotel_id, " +
                    "hotel.name as hotel_name, " +
                    "city.city_id as city_id, " +
                    "city.name as city_name, " +
                    "country.country_id as country_id " +
                    "from hotel hotel " +
                    "join hotel_address hotel_address " +
                    "on hotel.hotel_id = hotel_address.hotel_id " +
                    "join country country " +
                    "on hotel_address.country_id = country.country_id " +
                    "join city city " +
                    "on hotel_address.city_id = city.city_id " +
                    "join room room " +
                    "on hotel.hotel_id = room.hotel_id " +
                    "join room_info room_info " +
                    "on room.room_id = room_info.room_id " +
                    "where country.name = ? and room_info.adults_capacity = ? " +
                    "and room_info.children_capacity = ? and room.is_available = 1 " +
                    "order by city.name";

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
            logger.error("Error while creating/updating hotel", exception);
        }
    }

    @Override
    public Map<String, List<Hotel>> searchForHotels(String countryName, int childrenCapacity, int adultCapacity) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
             PreparedStatement stmtSelectHotelsByCountry = connection.prepareStatement(SEARCH_FOR_HOTELS)) {

            stmtSelectHotelsByCountry.setString(1, countryName);
            stmtSelectHotelsByCountry.setInt(2, adultCapacity);
            stmtSelectHotelsByCountry.setInt(3, childrenCapacity);
            ResultSet resultSet = stmtSelectHotelsByCountry.executeQuery();
            Map<String, List<Hotel>> result = new HashMap<>();

            while (resultSet.next()) {
                Hotel hotel = new Hotel();
                hotel.setId(resultSet.getInt("hotel_id"));
                hotel.setName(resultSet.getString("hotel_name"));
                HotelAddress hotelAddress = new HotelAddress();
                hotelAddress.setCountryId(resultSet.getInt("country_id"));
                hotelAddress.setCityId(resultSet.getInt("city_id"));
                hotel.setHotelAddress(hotelAddress);
                String cityName = resultSet.getString("city_name");
                boolean hasCity = result.containsKey(cityName);
                if (hasCity) {
                    List<Hotel> hotels = result.get(cityName);
                    hotels.add(hotel);
                } else {
                    List<Hotel> hotels = new ArrayList<>();
                    hotels.add(hotel);
                    result.put(cityName, hotels);
                }
            }
            if (result.isEmpty()) {
                return Collections.emptyMap();
            } else {
                return result;
            }
        } catch (Exception exception) {
            logger.error("error while searching for hotels ", exception);
            return Collections.emptyMap();
        }
    }

    @Override
    public Optional<Hotel> getHotel(int id) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
             PreparedStatement stmtSelectHotel = connection.prepareStatement(SELECT_HOTEL)) {
            stmtSelectHotel.setInt(1, id);
            ResultSet resultSet = stmtSelectHotel.executeQuery();
            if (resultSet.next()) {
                Hotel hotel = new Hotel();
                hotel.setId(id);
                hotel.setName(resultSet.getString("hotel_name"));

                HotelInfo hotelInfo = new HotelInfo();
                hotelInfo.setDetails(resultSet.getString("details"));
                hotelInfo.setHasWifi(resultSet.getBoolean("wifi"));
                hotelInfo.setHasParking(resultSet.getBoolean("parking"));
                hotelInfo.setHasSwimmingPool(resultSet.getBoolean("swimming_pool"));
                hotel.setHotelInfo(hotelInfo);
                return Optional.of(hotel);
            } else {
                throw new Exception("Couldn't get a hotel");
            }
        } catch (Exception exception) {
            logger.error("Error while trying to get hotel with the id {} ", id, exception);
            return Optional.empty();
        }
    }
}
