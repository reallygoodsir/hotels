package org.hotels.dao;

import java.math.BigDecimal;
import java.util.Date;

public interface TransactionDAO extends GeneralDAO{
    void executeTransaction(BigDecimal totalPrice, Date checkIn, Date checkOut,
                            int customerId, int hotelId, int roomId);
}
