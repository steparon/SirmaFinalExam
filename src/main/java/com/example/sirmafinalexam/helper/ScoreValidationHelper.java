package com.example.sirmafinalexam.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScoreValidationHelper {
    public static final Pattern patternNoPenalty = Pattern.compile("^\\d{1,2}-\\d{1,2}$");

    public static final Pattern patternWithPenalty = Pattern.compile("^\\d{1,2}\\(\\d{1,2}\\)-\\d{1,2}\\(\\d{1,2}\\)$");

    public static void validateScore(String score) {
        Matcher matcherA = patternNoPenalty.matcher(score);
        Matcher matcherB = patternWithPenalty.matcher(score);
        if (!matcherA.matches() && !matcherB.matches()) {
            throw new IllegalArgumentException("Not a valid format of the score");
        }
    }
}
