package com.example.sirmafinalexam.service;

import com.example.sirmafinalexam.helper.NumOfFieldsValidationHelper;
import com.example.sirmafinalexam.helper.TeamGroupValidationHelper;
import com.example.sirmafinalexam.model.Team;
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
public class TeamService {
    private final TeamRepository teamRepository;

    @Autowired
    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public List<Team> addTeams(MultipartFile file) {
        List<Team> teams = importTeams(file);
        if (!teams.isEmpty()) {
            teamRepository.saveAll(teams);
        }
        return teams;
    }

    public List<Team> importTeams(MultipartFile file) {
        List<Team> teams = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean skipHeader = true;
            while ((line = reader.readLine()) != null) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }

                String[] fields = line.split(",");
                Team team = assignFields(fields);

                teams.add(team);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return teams;
    }

    public Team assignFields(String[] fields) {
        NumOfFieldsValidationHelper.validateNumOfFields(4, fields);

        try {
            int id = Integer.parseInt(fields[0]);
            if (teamRepository.existsById(id)) {
                throw new NoSuchElementException("Team with ID " + id + " already exists");
            }
            String name = fields[1].trim();
            String managerFullName = fields[2].trim();
            String group = fields[3].trim();

            TeamGroupValidationHelper.validateTeamGroup(group);

            Team team = new Team();
            team.setId(id);
            team.setName(name);
            team.setManagerFullName(managerFullName);
            team.setGroup(group);

            return team;

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while assigning player fields: " + e.getMessage(), e);
        }
    }
}

