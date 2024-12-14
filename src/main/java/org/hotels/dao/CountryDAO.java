package org.hotels.dao;

import org.hotels.models.Country;

public interface CountryDAO extends GeneralDAO {
    boolean create(Country country);

    int getByName(String name);
}
