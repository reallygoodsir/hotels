package org.hotels.dao;

import org.hotels.models.Hotel;

public interface HotelDAO extends GeneralDAO {
    void create(Hotel hotel);
}
