package com.example.sirmafinalexam.helper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateValidationHelper {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");

    public static LocalDate parseDate(String date) {
        try {
            LocalDate parsedDate = LocalDate.parse(date, formatter);
            return parsedDate;
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid data format");
        }
    }
}
