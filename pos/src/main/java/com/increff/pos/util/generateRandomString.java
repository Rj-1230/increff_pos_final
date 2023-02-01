package com.increff.pos.util;
import java.util.UUID;
public class generateRandomString {
        public static String createRandomOrderCode() {
            UUID uuid=UUID.randomUUID();
            return uuid.toString();
        }

}
