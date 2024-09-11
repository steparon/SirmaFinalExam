package com.example.sirmafinalexam.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerPositionValidationHelper {

    public static final Pattern playerPosition = Pattern.compile("^(MF|FW|DF|GK)$");

    public static void validatePosition(String position) {
        Matcher matcher = playerPosition.matcher(position.trim());
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid position: " + position);
        }
    }
}
