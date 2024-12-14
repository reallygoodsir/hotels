package org.hotels.dao;

public interface StreetDAO extends GeneralDAO {
    int getByName(String name, int cityId);
}
