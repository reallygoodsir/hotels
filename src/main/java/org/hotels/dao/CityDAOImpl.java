package org.hotels.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CityDAOImpl implements CityDAO {
    private static final Logger logger = LogManager.getLogger(CityDAOImpl.class);
    private static final String SELECT_CITY_BY_NAME = "select * from city " +
            "where name = ?";

    @Override
    public int getByName(String name) {
        try (
                Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
                PreparedStatement stmtSelectStreet = connection.prepareStatement(SELECT_CITY_BY_NAME)
        ) {
            stmtSelectStreet.setString(1, name);
            ResultSet resultSet = stmtSelectStreet.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                throw new Exception("City couldn't be find");
            }
        } catch (Exception exception) {
            logger.error("Error while getting a city with the name {} ", name, exception);
            return -1;
        }
    }
}
