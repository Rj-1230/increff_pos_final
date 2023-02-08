package com.increff.pos.util;

import java.time.ZonedDateTime;

import static com.increff.pos.util.GetCurrentDateTime.convertStringToZonedDateTime;

public class DummyUtil {
    public static ZonedDateTime putDummyOrderInvoiceTime() {
        String date = "2023-01-01 00:00:00";
        return convertStringToZonedDateTime(date);
    }
}
