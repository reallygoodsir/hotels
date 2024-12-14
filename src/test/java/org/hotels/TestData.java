package org.hotels;

import org.hotels.dao.*;
import org.hotels.models.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestData {
    public static void main(String[] args) {
        // Create testing data. Create countries, cities, streets, hotels, rooms
        createData1();
        createData2();
    }

    private static void createData1() {
        CountryDAO countryDAO = new CountryDAOImpl();
        Country country = createCountry("USA", "NY", Arrays.asList("Adams Street", "Wilson Ave"));

        boolean isCountryCreated = countryDAO.create(country);
        if (isCountryCreated) {
            Hotel hotel = createHotel("Aloft Harlem");

            RoomInfo room1Info = createRoomInfo("Single", BigDecimal.valueOf(180),
                    true, 0, 1, "room has view");
            Room room1 = createRoom(true, "101", room1Info);

            RoomInfo room2Info = createRoomInfo("Deluxe", BigDecimal.valueOf(320), true,
                    1, 2, "room has a fridge");
            Room room2 = createRoom(true, "102", room2Info);

            RoomInfo room3Info = createRoomInfo("Deluxe", BigDecimal.valueOf(360),
                    true, 2, 2, "room has two bathrooms");
            Room room3 = createRoom(true, "103", room3Info);

            List<Room> rooms = new ArrayList<>();
            rooms.add(room1);
            rooms.add(room2);
            rooms.add(room3);
            hotel.setRooms(rooms);

            int countryId = countryDAO.getByName("USA");
            CityDAO cityDAO = new CityDAOImpl();
            int cityId = cityDAO.getByName("NY");
            StreetDAO streetDAO = new StreetDAOImpl();
            int streetId = streetDAO.getByName("Adams Street", cityId);

            HotelAddress hotelAddress = createHotelAddress(countryId, cityId, streetId);
            hotel.setHotelAddress(hotelAddress);

            HotelInfo hotelInfo = createHotelInfo("free food", true,
                    true, "23913823", true);
            hotel.setHotelInfo(hotelInfo);

            HotelDAOImpl hotelDAO = new HotelDAOImpl();
            hotelDAO.create(hotel);
        }
    }
    private static void createData2() {
        CountryDAO countryDAO = new CountryDAOImpl();
        Country country = createCountry("USA", "LA", Arrays.asList("Alameda Street", "Rodeo Drive"));

        boolean isCountryCreated = countryDAO.create(country);
        if (isCountryCreated) {
            Hotel hotel = createHotel("InterContinental");

            RoomInfo room1Info = createRoomInfo("Single", BigDecimal.valueOf(140),
                    true, 0, 1, "room has a premium tv");
            Room room1 = createRoom(true, "201", room1Info);

            RoomInfo room2Info = createRoomInfo("Deluxe", BigDecimal.valueOf(230), true,
                    1, 2, "room has a two fridges");
            Room room2 = createRoom(true, "202", room2Info);

            RoomInfo room3Info = createRoomInfo("Deluxe", BigDecimal.valueOf(255),
                    true, 2, 2, "room has five rooms");
            Room room3 = createRoom(true, "203", room3Info);

            List<Room> rooms = new ArrayList<>();
            rooms.add(room1);
            rooms.add(room2);
            rooms.add(room3);
            hotel.setRooms(rooms);

            int countryId = countryDAO.getByName("USA");
            CityDAO cityDAO = new CityDAOImpl();
            int cityId = cityDAO.getByName("LA");
            StreetDAO streetDAO = new StreetDAOImpl();
            int streetId = streetDAO.getByName("Alameda Street", cityId);

            HotelAddress hotelAddress = createHotelAddress(countryId, cityId, streetId);
            hotel.setHotelAddress(hotelAddress);

            HotelInfo hotelInfo = createHotelInfo("free spa", true,
                    true, "23959631", true);
            hotel.setHotelInfo(hotelInfo);

            HotelDAOImpl hotelDAO = new HotelDAOImpl();
            hotelDAO.create(hotel);
        }
    }
    private static Country createCountry(String countryName,
                                         String cityName,
                                         List<String> cityStreets) {
        List<Street> streets = new ArrayList<>();
        for (String cityStreet : cityStreets) {
            Street street = new Street();
            street.setName(cityStreet);
            streets.add(street);
        }

        City city = new City();
        city.setName(cityName);
        city.setStreets(streets);

        Country country = new Country();
        country.setName(countryName);
        List<City> cities = new ArrayList<>();
        cities.add(city);

        country.setCities(cities);
        return country;
    }

    private static Hotel createHotel(String hotelName) {
        Hotel hotel = new Hotel();
        hotel.setName(hotelName);
        return hotel;
    }

    private static HotelInfo createHotelInfo(String details,
                                             boolean hasParking,
                                             boolean hasSwimmingPool,
                                             String phoneNumber,
                                             boolean hasWifi) {
        HotelInfo hotelInfo = new HotelInfo();
        hotelInfo.setDetails(details);
        hotelInfo.setHasParking(hasParking);
        hotelInfo.setHasSwimmingPool(hasSwimmingPool);
        hotelInfo.setPhoneNumber(phoneNumber);
        hotelInfo.setHasWifi(hasWifi);
        return hotelInfo;
    }

    private static HotelAddress createHotelAddress(int countryId,
                                                   int cityId,
                                                   int streetId) {
        HotelAddress hotelAddress = new HotelAddress();
        hotelAddress.setCountryId(countryId);
        hotelAddress.setCityId(cityId);
        hotelAddress.setStreetId(streetId);
        return hotelAddress;
    }

    private static Room createRoom(boolean isAvailable,
                                   String roomNumber,
                                   RoomInfo roomInfo) {
        Room room = new Room();
        room.setAvailable(isAvailable);
        room.setRoomNumber(roomNumber);
        room.setRoomInfo(roomInfo);
        return room;
    }

    private static RoomInfo createRoomInfo(String roomType,
                                           BigDecimal pricePerNight,
                                           boolean hasAirConditioning,
                                           int childrenCapacity,
                                           int adultsCapacity,
                                           String details) {
        RoomInfo roomInfo = new RoomInfo();
        roomInfo.setRoomType(roomType);
        roomInfo.setPricePerNight(pricePerNight);
        roomInfo.setHasAirConditioning(hasAirConditioning);
        roomInfo.setChildrenCapacity(childrenCapacity);
        roomInfo.setAdultsCapacity(adultsCapacity);
        roomInfo.setDetails(details);
        return roomInfo;
    }
}
