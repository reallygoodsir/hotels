package org.hotels.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CityDAOImpl implements CityDAO {
    private static final String SELECT_CITY_BY_NAME = "select * from city " +
            "where name = ?";
    private static final String SELECT_CITY_BY_ID = "select name from city " +
            "where city_id = ?";

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
            }
            throw new Exception("city doesnt exist");
        } catch (Exception exception) {
            System.err.println("citydao error " + exception.getMessage());
            return -1;
        }
    }

    @Override
    public String getById(int id) {
        try (
                Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
                PreparedStatement stmtSelectStreet = connection.prepareStatement(SELECT_CITY_BY_ID)
        ) {
            stmtSelectStreet.setInt(1, id);
            ResultSet resultSet = stmtSelectStreet.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString(1);
            }
            throw new Exception("city doesnt exist");
        } catch (Exception exception) {
            System.err.println("citydao error " + exception.getMessage());
            return null;
        }
    }
}
