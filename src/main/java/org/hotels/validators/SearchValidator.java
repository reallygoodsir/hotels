package org.hotels.validators;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class SearchValidator {
    public boolean isCheckInValid(String checkIn) {
        if (checkIn == null || checkIn.isEmpty()) {
            return false;
        }

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(checkIn);
            return date != null;
        } catch (ParseException e) {
            return false;
        }
    }

    public boolean isCheckOutValid(String checkOut) {
        if (checkOut == null || checkOut.isEmpty()) {
            return false;
        }

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(checkOut);
            return date != null;
        } catch (ParseException e) {
            return false;
        }
    }

    public boolean isChildrenCapacityValid(String childrenCapacity) {
        if (childrenCapacity == null || childrenCapacity.isEmpty()) {
            return false;
        }
        int childrenCapacityNumber = Integer.parseInt(childrenCapacity);
        return childrenCapacityNumber >= 0;
    }

    public boolean isAdultCapacityValid(String adultCapacity) {
        if (adultCapacity == null || adultCapacity.isEmpty()) {
            return false;
        }
        int adultCapacityNumber = Integer.parseInt(adultCapacity);
        return adultCapacityNumber > 0;
    }

    public int isDayDistanceValid(Date checkIn, Date checkOut) {
        try {
            LocalDate checkInLocalDate = checkIn.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            LocalDate checkOutLocalDate = checkOut.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            long dayDistance = ChronoUnit.DAYS.between(checkInLocalDate, checkOutLocalDate);
            if (dayDistance == 0) {
                return -1;
            } else if (dayDistance < 0) {
                return -2;
            } else {
                return (int) dayDistance;
            }
        } catch (Exception exception) {
            return -3;
        }
    }
}