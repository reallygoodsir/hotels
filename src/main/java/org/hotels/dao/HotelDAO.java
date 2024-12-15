package org.hotels.dao;

import org.hotels.models.Hotel;

import java.util.List;
import java.util.Map;

public interface HotelDAO extends GeneralDAO {
    void create(Hotel hotel);

    Map<String, List<Hotel>> searchForHotels(String countryName, int childrenCapacity, int adultCapacity);
}
