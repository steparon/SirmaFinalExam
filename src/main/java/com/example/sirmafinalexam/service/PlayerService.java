package com.example.sirmafinalexam.service;

import com.example.sirmafinalexam.helper.NumOfFieldsValidationHelper;
import com.example.sirmafinalexam.helper.PlayerPositionValidationHelper;
import com.example.sirmafinalexam.model.Player;
import com.example.sirmafinalexam.model.Team;
import com.example.sirmafinalexam.repository.PlayerRepository;
import com.example.sirmafinalexam.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    private final TeamRepository teamRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository, TeamRepository teamRepository) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
    }

    public List<Player> addPlayers(MultipartFile file) {
        List<Player> players = importPlayers(file);
        if (!players.isEmpty()) {
            playerRepository.saveAll(players);
        }
        return players;
    }

    public List<Player> importPlayers(MultipartFile file) {
        List<Player> players = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean skipHeader = true;
            while ((line = reader.readLine()) != null) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }
                String[] fields = line.split(",");
                players.add(assignFields(fields));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read the file: " + e.getMessage(), e);
        }
        return players;
    }

    public Team getTeamById(Integer teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new NoSuchElementException("Team with ID " + teamId + " doesn't exist"));
    }

    public Player assignFields(String[] fields) {
        NumOfFieldsValidationHelper.validateNumOfFields(5, fields);

        try {
            int id = Integer.parseInt(fields[0]);
            if (playerRepository.existsById(id)) {
                throw new IllegalArgumentException("Player with ID " + id + " already exists");
            }

            int teamNumber = parseTeamNumber(fields[1]);
            String position = fields[2].trim();

            PlayerPositionValidationHelper.validatePosition(position);

            String fullName = fields[3].trim();
            int teamId = parseTeamId(fields[4]);

            Player player = new Player();
            player.setId(id);
            player.setTeamNumber(teamNumber);
            player.setPosition(position);
            player.setFullName(fullName);

            Team team = getTeamById(teamId);
            player.setTeam(team);

            return player;

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while assigning player fields: " + e.getMessage(), e);
        }
    }

    private int parseTeamNumber(String teamNumberField) {
        try {
            return Integer.parseInt(teamNumberField.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid team number: " + teamNumberField, e);
        }
    }

    private int parseTeamId(String teamIdField) {
        try {
            return Integer.parseInt(teamIdField.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid team ID: " + teamIdField, e);
        }
    }
}

