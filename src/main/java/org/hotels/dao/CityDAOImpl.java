package org.hotels.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CityDAOImpl implements CityDAO {
    private static final String SELECT_CITY = "select * from city " +
            "where name = ?";

    @Override
    public int getByName(String name) {
        try (
                Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
                PreparedStatement stmtSelectStreet = connection.prepareStatement(SELECT_CITY)
        ) {
            stmtSelectStreet.setString(1, name);
            ResultSet resultSet = stmtSelectStreet.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            throw new Exception("country doesnt exist");
        } catch (Exception exception) {
            System.err.println("citydao error " + exception.getMessage());
            return -1;
        }
    }
}
