package org.hotels.models;

import java.math.BigDecimal;
import java.util.Date;

public class Transaction {
    private final int roomId;
    private int hotelId;
    private int customerId;
    private final BigDecimal totalPrice;
    private final Date checkIn;
    private final Date checkOut;

    public Transaction(int roomId, int hotelId, BigDecimal totalPrice, Date checkIn, Date checkOut) {
        this.roomId = roomId;
        this.hotelId = hotelId;
        this.totalPrice = totalPrice;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public int getRoomId() {
        return roomId;
    }

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public Date getCheckIn() {
        return checkIn;
    }

    public Date getCheckOut() {
        return checkOut;
    }
}
