package it.unipi.gamegram.utility;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

// Utility class to convert dates between code and db
public class DateConverter {
    public static LocalDate convertToLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static Date convertLocalDateToDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}