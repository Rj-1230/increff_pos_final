package com.increff.pos.util;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class GetCurrentDateTime {

    public static LocalDate getLocalDate()
    {
        return LocalDate.now();
    }

    public static ZonedDateTime convertStringToZonedDateTime(String dateTime){
        DateTimeFormatter formatter  = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ZoneId timeZone = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = LocalDateTime.parse(dateTime, formatter).atZone(timeZone);
        return zonedDateTime;
    }

    public static ZonedDateTime getStartOfDay(){
        LocalDate localDate = LocalDate.now();
//Current zone
        ZonedDateTime startOfDayInZone = localDate.atStartOfDay(ZoneId.systemDefault());
        return  startOfDayInZone;
    }
    public static ZonedDateTime getEndOfDay(){
        LocalDate localDate = LocalDate.now();
//Current zone
        ZonedDateTime endOfDayInZone = localDate.atTime(LocalTime.MAX)
                .atZone(ZoneId.systemDefault());
        return endOfDayInZone;
    }
}