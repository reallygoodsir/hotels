package org.hotels.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotels.models.Customer;
import org.hotels.models.Transaction;

import java.sql.*;
import java.util.UUID;

public class TransactionDAOImpl implements TransactionDAO {
    private static final Logger logger = LogManager.getLogger(TransactionDAOImpl.class);
    private static final String SELECT_CUSTOMER = "select customer_id from customer " +
            "where name = ? or email = ?";
    private static final String INSERT_CUSTOMER = "insert into customer " +
            "(name, email, phone_number) " +
            "values (?, ?, ?)";
    private static final String INSERT_TRANSACTION = "insert into transaction " +
            "(hotel_id, room_id, customer_id, total_price, check_in, check_out, confirmation_number) " +
            "values (?, ?, ?, ?, ?, ?, ?)";

    @Override
    public String executeTransaction(Customer customer, Transaction transaction) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD)) {
            try (PreparedStatement stmtSelectCustomer = connection.prepareStatement(SELECT_CUSTOMER)) {

                stmtSelectCustomer.setString(1, customer.getName());
                stmtSelectCustomer.setString(2, customer.getEmail());
                ResultSet resultSet = stmtSelectCustomer.executeQuery();
                if (resultSet.next()) {
                    transaction.setCustomerId(resultSet.getInt(1));
                } else {
                    try (PreparedStatement stmtInsertCustomer
                                 = connection.prepareStatement(INSERT_CUSTOMER, Statement.RETURN_GENERATED_KEYS)) {
                        connection.setAutoCommit(false);

                        stmtInsertCustomer.setString(1, customer.getName());
                        stmtInsertCustomer.setString(2, customer.getEmail());
                        stmtInsertCustomer.setString(3, customer.getPhoneNumber());
                        stmtInsertCustomer.executeUpdate();
                        try (ResultSet generatedKeys = stmtInsertCustomer.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                transaction.setCustomerId(generatedKeys.getInt(1));
                            } else {
                                connection.rollback();
                                throw new Exception("Couldn't get generated keys");
                            }
                        }
                    }
                }
            }

            try (PreparedStatement stmtInsertTransaction = connection.prepareStatement(INSERT_TRANSACTION)) {
                connection.setAutoCommit(false);
                stmtInsertTransaction.setInt(1, transaction.getHotelId());
                stmtInsertTransaction.setInt(2, transaction.getRoomId());
                stmtInsertTransaction.setInt(3, transaction.getCustomerId());
                stmtInsertTransaction.setBigDecimal(4, transaction.getTotalPrice());

                java.sql.Date checkInSql = new java.sql.Date(transaction.getCheckIn().getTime());
                stmtInsertTransaction.setDate(5, checkInSql);
                java.sql.Date checkOutSql = new java.sql.Date(transaction.getCheckOut().getTime());
                stmtInsertTransaction.setDate(6, checkOutSql);
                UUID confirmationNumber = UUID.randomUUID();
                stmtInsertTransaction.setString(7, confirmationNumber.toString());
                stmtInsertTransaction.executeUpdate();
                connection.commit();
                return confirmationNumber.toString();
            }
        } catch (Exception exception) {
            logger.error("Error while trying to calculate the total price", exception);
            return null;
        }
    }
}
