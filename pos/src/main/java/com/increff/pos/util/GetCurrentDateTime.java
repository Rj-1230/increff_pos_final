package com.increff.pos.util;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class GetCurrentDateTime {
    public static String get_current_dat_time() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public static LocalDate getLocalDate()
    {
        return LocalDate.now();
    }
}