package org.hotels.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotels.models.City;
import org.hotels.models.Country;
import org.hotels.models.Street;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CountryDAOImpl implements CountryDAO {
    private static final Logger logger = LogManager.getLogger(CountryDAOImpl.class);
    private static final String SELECT_ALL_COUNTRIES = "select name from hotels_db.country";
    private static final String SELECT_COUNTRY_BY_NAME = "select * from country " +
            "where name = ?";
    private static final String INSERT_COUNTRY = "insert into country " +
            "(name) " +
            "values(?)";
    private static final String SELECT_CITY = "select * from city " +
            "where name = ?";
    private static final String INSERT_CITY = "insert into city " +
            "(country_id, name) " +
            "values(?, ?)";
    private static final String SELECT_STREET = "select * from street " +
            "where name = ? and city_id = ?";
    private static final String INSERT_STREET = "insert into street " +
            "(city_id, name) " +
            "values(?, ?)";


    public boolean create(Country country) {
        try (
                Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD)
        ) {
            connection.setAutoCommit(false);
            int countryId = 0;
            try (PreparedStatement stmtSelectCountry = connection.prepareStatement(SELECT_COUNTRY_BY_NAME)) {
                stmtSelectCountry.setString(1, country.getName());
                try (ResultSet resultSet = stmtSelectCountry.executeQuery()) {
                    if (resultSet.next()) {
                        countryId = resultSet.getInt(1);
                    }
                }
            }
            if (countryId == 0) {
                try (PreparedStatement stmtCreateCountry =
                             connection.prepareStatement(INSERT_COUNTRY, Statement.RETURN_GENERATED_KEYS)) {
                    stmtCreateCountry.setString(1, country.getName());
                    stmtCreateCountry.executeUpdate();
                    try (ResultSet generatedKeys = stmtCreateCountry.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            countryId = generatedKeys.getInt(1);
                        } else {
                            connection.rollback();
                            throw new Exception("Couldn't get generated keys");
                        }
                    }
                }
            }

            List<City> cities = country.getCities();
            for (City city : cities) {

                int cityId = 0;
                try (PreparedStatement stmtSelectCity = connection.prepareStatement(SELECT_CITY)) {
                    stmtSelectCity.setString(1, city.getName());
                    try (ResultSet resultSet = stmtSelectCity.executeQuery()) {
                        if (resultSet.next()) {
                            cityId = resultSet.getInt(1);
                        }
                    }
                }
                if (cityId == 0) {
                    try (PreparedStatement stmtCreateCity =
                                 connection.prepareStatement(INSERT_CITY, Statement.RETURN_GENERATED_KEYS)) {
                        stmtCreateCity.setInt(1, countryId);
                        stmtCreateCity.setString(2, city.getName());
                        stmtCreateCity.executeUpdate();
                        try (ResultSet generatedKeys = stmtCreateCity.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                cityId = generatedKeys.getInt(1);
                            } else {
                                connection.rollback();
                                throw new Exception("Couldn't get generated keys");
                            }
                        }
                    }
                }
                List<Street> streets = city.getStreets();
                for (Street street : streets) {
                    int streetId = 0;
                    try (PreparedStatement stmtSelectStreet = connection.prepareStatement(SELECT_STREET)) {
                        stmtSelectStreet.setString(1, street.getName());
                        stmtSelectStreet.setInt(2, cityId);
                        try (ResultSet resultSet = stmtSelectStreet.executeQuery()) {
                            if (resultSet.next()) {
                                streetId = resultSet.getInt(1);
                            }
                        }
                    }
                    if (streetId == 0) {
                        try (PreparedStatement stmtCreateStreet =
                                     connection.prepareStatement(INSERT_STREET, Statement.RETURN_GENERATED_KEYS)) {
                            stmtCreateStreet.setInt(1, cityId);
                            stmtCreateStreet.setString(2, street.getName());
                            stmtCreateStreet.executeUpdate();
                            try (ResultSet generatedKeys = stmtCreateStreet.getGeneratedKeys()) {
                                if (!generatedKeys.next()) {
                                    connection.rollback();
                                    throw new Exception("Couldn't get generated keys");
                                }
                            }
                        }
                    }
                }
            }
            connection.commit();
            return true;
        } catch (Exception exception) {
            logger.error("Error during creating a country", exception);
            return false;
        }
    }

    @Override
    public List<Country> getAllCountries() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
             PreparedStatement stmtSelectAllCountries = connection.prepareStatement(SELECT_ALL_COUNTRIES)) {

            ResultSet resultSet = stmtSelectAllCountries.executeQuery();
            List<Country> countries = new ArrayList<>();
            while (resultSet.next()) {
                Country country = new Country();
                country.setName(resultSet.getString("name"));
                countries.add(country);
            }
            if (countries.isEmpty()) {
                throw new Exception("Couldn't get any countries");
            } else {
                return countries;
            }
        } catch (Exception exception) {
            logger.error("Error while getting all countries ", exception);
            return Collections.emptyList();
        }
    }

    @Override
    public int getByName(String name) {
        try (
                Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
                PreparedStatement stmtSelectStreet = connection.prepareStatement(SELECT_COUNTRY_BY_NAME)
        ) {
            stmtSelectStreet.setString(1, name);
            ResultSet resultSet = stmtSelectStreet.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                throw new Exception("country doesnt exist");
            }
        } catch (Exception exception) {
            logger.error("Error while getting the city with the name {}", name, exception);
            return -1;
        }
    }
}
