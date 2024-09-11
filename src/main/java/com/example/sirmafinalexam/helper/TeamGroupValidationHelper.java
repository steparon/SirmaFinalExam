package com.example.sirmafinalexam.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TeamGroupValidationHelper {

    public static final Pattern teamGroupPattern = Pattern.compile("^[A-F]$");

    public static void validateTeamGroup(String teamGroup) {
        Matcher matcher = teamGroupPattern.matcher(teamGroup);
        if (!matcher.matches()) {
            System.out.println("Invalid team group: " + teamGroup);
        }
    }
}
