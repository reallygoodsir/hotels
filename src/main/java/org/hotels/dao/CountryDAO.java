package org.hotels.dao;

import org.hotels.models.Country;

import java.util.List;

public interface CountryDAO extends GeneralDAO {
    boolean create(Country country);
    List<Country> getAllCountries();
    int getByName(String name);
    String getById(int id);
}
