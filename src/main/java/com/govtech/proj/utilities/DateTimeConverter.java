package com.govtech.proj.utilities;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateTimeConverter {

    public static String singaporeZone = "Asia/Singapore";
    public static String formattedPattern = "dd-MM-yyyy HH:mm:ss";

    public static LocalDateTime getCurrentTime() {
        return LocalDateTime.now(ZoneId.of(singaporeZone));
    }

    public static String formatDateTime(LocalDateTime localDateTime) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formattedPattern);
        String formattedTime = localDateTime.format(formatter);

        return formattedTime;
    }
}
