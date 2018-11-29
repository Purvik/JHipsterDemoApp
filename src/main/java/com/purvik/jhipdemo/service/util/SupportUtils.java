package com.purvik.jhipdemo.service.util;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


public class SupportUtils {

    public static ZonedDateTime convertStringDateToZoneDateTime(String date) {
        return date != "" && date != null ? LocalDate.parse(date.trim(), DateTimeFormatter.ofPattern("MM/dd/yyyy")).atStartOfDay(ZoneOffset.UTC) : null;
    }

    public static LocalDate convertStringDateToLocalDate(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    }

    public static String convertZoneDateTimeToString(ZonedDateTime date) {
        return date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    }

    public static String convertLocalDateToString(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    }
}
