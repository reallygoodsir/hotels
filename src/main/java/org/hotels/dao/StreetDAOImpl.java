package org.hotels.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StreetDAOImpl implements StreetDAO {
    private static final String SELECT_STREET = "select * from street " +
            "where name = ? and city_id = ?";

    @Override
    public int getByName(String name, int cityId) {
        try (
                Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
                PreparedStatement stmtSelectStreet = connection.prepareStatement(SELECT_STREET)
        ) {
            stmtSelectStreet.setString(1, name);
            stmtSelectStreet.setInt(2, cityId);
            ResultSet resultSet = stmtSelectStreet.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            throw new Exception("country doesnt exist");
        } catch (Exception exception) {
            System.err.println("streetdao error " + exception.getMessage());
            return -1;
        }
    }
}
