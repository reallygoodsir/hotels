package org.hotels.dao;

public interface CityDAO extends GeneralDAO {

    int getByName(String name);
}
