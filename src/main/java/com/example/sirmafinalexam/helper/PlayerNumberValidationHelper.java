package com.example.sirmafinalexam.helper;

import java.util.regex.Pattern;

public class PlayerNumberValidationHelper {

    public static final Pattern playerNumberPattern = Pattern.compile("^(2[0-6]|1[0-9]|[1-9])$");

    public static void validatePlayerNumber(String playerNumber) {
        if (!playerNumberPattern.matcher(playerNumber).matches()) {
            throw new IllegalArgumentException(playerNumber + " is not a valid player number");
        }
    }
}
