package org.hotels.dao;

import java.sql.*;

public class CustomerDAOImpl implements CustomerDAO {
    private static final String SELECT_CUSTOMER = "select customer_id from customer " +
            "where name = ? or email = ?";
    private static final String INSERT_CUSTOMER = "insert into customer " +
            "(name, email, phone_number) " +
            "values (?, ?, ?)";

    @Override
    public int createCustomer(String email, String name, String phoneNumber) {

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD)) {
            try(PreparedStatement stmtSelectCustomer = connection.prepareStatement(SELECT_CUSTOMER)){
                stmtSelectCustomer.setString(1, name);
                stmtSelectCustomer.setString(2, email);
                ResultSet resultSet = stmtSelectCustomer.executeQuery();
                if(resultSet.next()){
                    return resultSet.getInt(1);
                }else{
                    try (PreparedStatement stmtInsertCustomer
                                 = connection.prepareStatement(INSERT_CUSTOMER, Statement.RETURN_GENERATED_KEYS)) {
                        connection.setAutoCommit(false);

                        stmtInsertCustomer.setString(1, name);
                        stmtInsertCustomer.setString(2, email);
                        stmtInsertCustomer.setString(3, phoneNumber);
                        stmtInsertCustomer.executeUpdate();
                        try (ResultSet generatedKeys = stmtInsertCustomer.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                connection.commit();
                                return generatedKeys.getInt(1);
                            } else {
                                connection.rollback();
                                throw new Exception("Couldn't get generated keys");
                            }
                        }
                    }catch (Exception exception) {
                        System.err.println("Error while creating a customer" + exception.getMessage());
                        return -1;
                    }
                }
            }
        } catch (Exception exception) {
            System.err.println("Error while trying to create customer");
            return -1;
        }
    }
}
