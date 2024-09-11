package com.example.sirmafinalexam.service;

import com.example.sirmafinalexam.helper.NumOfFieldsValidationHelper;
import com.example.sirmafinalexam.model.Match;
import com.example.sirmafinalexam.model.Player;
import com.example.sirmafinalexam.repository.MatchRepository;
import com.example.sirmafinalexam.repository.PlayerRepository;
import com.example.sirmafinalexam.repository.RecordRepository;
import com.example.sirmafinalexam.model.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class RecordService {

    private final PlayerRepository playerRepository;

    private final MatchRepository matchRepository;

    private final RecordRepository recordRepository;

    @Autowired
    public RecordService(PlayerRepository playerRepository, MatchRepository matchRepository, RecordRepository recordRepository) {
        this.playerRepository = playerRepository;
        this.matchRepository = matchRepository;
        this.recordRepository = recordRepository;
    }

    public List<Record> addRecords(MultipartFile file) {
        List<Record> records = importRecords(file);
        if (!records.isEmpty()) {
            recordRepository.saveAll(records);
        }
        return records;
    }

    public List<Record> importRecords(MultipartFile file) {
        List<Record> records = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean skipHeader = true;
            while ((line = reader.readLine()) != null) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }
                String[] fields = line.split(",");
                records.add(assignFields(fields));

            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read the file: " + e.getMessage(), e);
        }
        return records;
    }

    public Record assignFields(String[] fields) {
        NumOfFieldsValidationHelper.validateNumOfFields(5, fields);

        try {
            int id = Integer.parseInt(fields[0]);
            if (recordRepository.existsById(id)) {
                throw new IllegalArgumentException("Record with ID " + id + " already exists");
            }
            int playerId = Integer.parseInt(fields[1]);
            int matchId = Integer.parseInt(fields[2]);
            int fromMins = Integer.parseInt(fields[3]);
            int toMins;

            if (fields[4].equalsIgnoreCase("NULL")) {
                toMins = 90;
            } else {
                toMins= Integer.parseInt(fields[4]);
            }

            Record record = new Record();
            record.setId(id);
            Player player = playerRepository.findById(playerId).orElse(null);
            Match match = matchRepository.findById(matchId).orElse(null);
            record.setPlayer(player);
            record.setMatch(match);
            record.setStartMin(fromMins);
            record.setFinishMin(toMins);

            return record;

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while assigning player fields: " + e.getMessage(), e);
        }
    }
}


