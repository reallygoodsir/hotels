package org.hotels.dao;

import org.hotels.models.Hotel;

import java.util.Date;
import java.util.List;

public interface HotelDAO extends GeneralDAO {
    void create(Hotel hotel);
    List<Hotel> searchForHotels(Date check_in, Date check_out, String countryName,
                                int childrenCapacity, int adultCapacity);
}
