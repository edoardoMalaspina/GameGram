package it.unipi.gamegram;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateConverter {
    public static LocalDate convertToLocalDate(Date date) {
        if(date == null){
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static Date convertLocalDateToDate(LocalDate localDate) {
        if(localDate == null){
            return null;
        }
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
