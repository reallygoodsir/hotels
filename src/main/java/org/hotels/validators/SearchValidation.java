package org.hotels.validators;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class SearchValidation {
    public boolean validateCheckIn(String checkIn) {
        return checkIn != null && !checkIn.isEmpty();
    }

    public boolean validateCheckOut(String checkOut) {
        return checkOut != null && !checkOut.isEmpty();
    }

    public int validateChildrenCapacity(String childrenCapacity) {
        if(childrenCapacity == null || childrenCapacity.isEmpty()){
            return -1;
        }
        int childrenCapacityNumber = Integer.parseInt(childrenCapacity);
        if(childrenCapacityNumber < 0){
            return -1;
        }else{
            return childrenCapacityNumber;
        }
    }

    public int validateAdultCapacity(String adultCapacity) {
        if(adultCapacity == null || adultCapacity.isEmpty()){
            return -1;
        }
        int adultCapacityNumber = Integer.parseInt(adultCapacity);
        if(adultCapacityNumber < 0){
            return -1;
        }else{
            return adultCapacityNumber;
        }
    }

    public int validateAndCalculateDayDistance(Date checkIn, Date checkOut) {
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
        }catch (Exception exception){
            return -3;
        }
    }
}
