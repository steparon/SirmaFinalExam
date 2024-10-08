package com.example.sirmafinalexam.controller;

import com.example.sirmafinalexam.service.MatchService;
import com.example.sirmafinalexam.service.PlayerService;
import com.example.sirmafinalexam.service.RecordService;
import com.example.sirmafinalexam.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
public class FileUploadController {

    private final TeamService teamService;

    private final PlayerService playerService;

    private final MatchService matchService;

    private final RecordService recordService;

    @Autowired
    public FileUploadController(TeamService teamService, PlayerService playerService, MatchService matchService, RecordService recordService) {
        this.teamService = teamService;
        this.playerService = playerService;
        this.matchService = matchService;
        this.recordService = recordService;
    }

    @PostMapping
    public ResponseEntity<String> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        if (files.length != 4) {
            return ResponseEntity.badRequest().body("Please upload exactly 4 CSV files.");
        }

        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();

            try {
                if (fileName.endsWith("teams.csv")) {
                    teamService.addTeams(file);
                } else if (fileName.endsWith("players.csv")) {
                    playerService.addPlayers(file);
                } else if (fileName.endsWith("matches.csv")) {
                    matchService.addMatches(file);
                } else if (fileName.endsWith("records.csv")) {
                    recordService.addRecords(file);
                } else {
                    return ResponseEntity.badRequest().body("Unexpected file: " + fileName);
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error processing file " + fileName + ": " + e.getMessage());
            }
        }
        return ResponseEntity.ok("Files processed successfully.");
    }
}

