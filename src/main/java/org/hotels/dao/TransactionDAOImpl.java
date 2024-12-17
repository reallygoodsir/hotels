package org.hotels.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Date;

public class TransactionDAOImpl implements TransactionDAO{
    private static final String INSERT_TRANSACTION = "insert into transaction " +
            "(hotel_id, room_id, customer_id, total_price, check_in, check_out) " +
            "values (?, ?, ?, ?, ?, ?)";
    @Override
    public void executeTransaction(BigDecimal totalPrice, Date checkIn, Date checkOut,
                                   int customerId, int hotelId, int roomId) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
             PreparedStatement stmtInsertTransaction = connection.prepareStatement(INSERT_TRANSACTION)) {
            connection.setAutoCommit(false);
            stmtInsertTransaction.setInt(1, hotelId);
            stmtInsertTransaction.setInt(2, roomId);
            stmtInsertTransaction.setInt(3, customerId);
            stmtInsertTransaction.setBigDecimal(4,totalPrice);

            java.sql.Date checkInSql = new java.sql.Date(checkIn.getTime());
            stmtInsertTransaction.setDate(5, checkInSql);
            java.sql.Date checkOutSql = new java.sql.Date(checkOut.getTime());
            stmtInsertTransaction.setDate(6, checkOutSql);
            stmtInsertTransaction.executeUpdate();

            connection.commit();
        }catch (Exception exception){
            System.err.println("Error while trying to calculate the total price" + exception.getMessage());
            exception.printStackTrace();
        }
    }
}
