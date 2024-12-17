package org.hotels.dao;

import org.hotels.models.Room;

import java.util.List;

public interface RoomDAO extends GeneralDAO{
    List<Room> getRoomsForHotel(int hotelId, int adultCapacity, int childrenCapacity);
}
