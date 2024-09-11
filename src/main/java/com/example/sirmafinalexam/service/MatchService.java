package com.example.sirmafinalexam.service;

import com.example.sirmafinalexam.helper.DateValidationHelper;
import com.example.sirmafinalexam.helper.NumOfFieldsValidationHelper;
import com.example.sirmafinalexam.helper.ScoreValidationHelper;
import com.example.sirmafinalexam.model.Match;
import com.example.sirmafinalexam.model.Team;
import com.example.sirmafinalexam.repository.MatchRepository;
import com.example.sirmafinalexam.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class MatchService {

    private final TeamRepository teamRepository;

    private final MatchRepository matchRepository;

    @Autowired
    public MatchService(TeamRepository teamRepository, MatchRepository matchRepository) {
        this.teamRepository = teamRepository;
        this.matchRepository = matchRepository;
    }

    public List<Match> addMatches(MultipartFile file)  {
        List<Match> matches = importMatches(file);
        if (!matches.isEmpty()) {
            matchRepository.saveAll(matches);
        }
        return matches;
    }

    public List<Match> importMatches(MultipartFile file) {
        List<Match> matches = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean skipHeader = true;
            while ((line = reader.readLine()) != null) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }
                String[] fields = line.split(",");
                matches.add(assignFields(fields));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read the file: " + e.getMessage(), e);
        }
        return matches;
    }

    public Team getTeamById(Integer teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new NoSuchElementException("Team with ID " + teamId + " doesn't exist"));
    }

    public Match assignFields(String[] fields) {
        NumOfFieldsValidationHelper.validateNumOfFields(5, fields);

        try {
            int id = Integer.parseInt(fields[0]);
            if (matchRepository.existsById(id)) {
                throw new IllegalArgumentException("Match with ID " + id + " already exists");
            }

            int teamAId = parseTeamId(fields[1]);
            int teamBId = parseTeamId(fields[2]);
            LocalDate date = DateValidationHelper.parseDate(fields[3]);
            String score = fields[4];

            if (teamAId == teamBId) {
                throw new IllegalArgumentException("One team cannot play against itself :D");
            }

            ScoreValidationHelper.validateScore(score);

            Match match = new Match();
            match.setId(id);
            Team teamA = getTeamById(teamAId);
            Team teamB = getTeamById(teamBId);
            match.setTeamA(teamA);
            match.setTeamB(teamB);
            match.setDate(date);
            match.setScore(score);

            return match;

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while assigning player fields: " + e.getMessage(), e);
        }
    }

    private int parseTeamId(String teamIdField) {
        try {
            return Integer.parseInt(teamIdField.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid team ID: " + teamIdField, e);
        }
    }

    private Team getTeamById(int teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team with ID " + teamId + " does not exist."));
    }
}

