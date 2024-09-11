package com.example.sirmafinalexam.controller;

import com.example.sirmafinalexam.model.Player;
import com.example.sirmafinalexam.service.TotalMinutesPlayedByPair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Set;

@RestController
@RequestMapping("/result")
public class PairMinutesController {

    private final TotalMinutesPlayedByPair totalMinutesPlayedByPair;

    @Autowired
    public PairMinutesController(TotalMinutesPlayedByPair totalMinutesPlayedByPair) {
        this.totalMinutesPlayedByPair = totalMinutesPlayedByPair;
    }

    @GetMapping
    public ResponseEntity<String> getMaxTimePlayers() {
        HashMap<Set<Player>, HashMap<Integer, Integer>> playerPairMap = totalMinutesPlayedByPair.iterateThroughRecordsByMatch();

        Set<Player> maxTimePlayers = totalMinutesPlayedByPair.iterateMapToCountMaxMinsPerPair(playerPairMap);

        String result = totalMinutesPlayedByPair.getResultsForPrinting(maxTimePlayers, playerPairMap);

        return ResponseEntity.ok(result);
    }


}
