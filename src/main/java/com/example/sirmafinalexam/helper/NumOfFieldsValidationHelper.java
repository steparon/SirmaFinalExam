package com.example.sirmafinalexam.helper;

public class NumOfFieldsValidationHelper {

    public static void validateNumOfFields(int expectedNumber, String[] fields) {
        if (fields.length != expectedNumber) {
            throw new IllegalArgumentException("Number of arguments expected: " + expectedNumber +
                    ", number of arguments received" + fields.length);
        }
    }
}
