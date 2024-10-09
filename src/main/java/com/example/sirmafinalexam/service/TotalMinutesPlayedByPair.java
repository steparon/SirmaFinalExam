package com.example.sirmafinalexam.service;

import com.example.sirmafinalexam.model.Player;
import com.example.sirmafinalexam.repository.MatchRepository;
import com.example.sirmafinalexam.repository.PlayerRepository;
import com.example.sirmafinalexam.repository.RecordRepository;
import com.example.sirmafinalexam.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.sirmafinalexam.model.Record;

import java.util.*;

@Service
public class TotalMinutesPlayedByPair {

    private final TeamRepository teamRepository;

    private final PlayerRepository playerRepository;

    private final MatchRepository matchRepository;

    private final RecordRepository recordRepository;

    @Autowired
    public TotalMinutesPlayedByPair(TeamRepository teamRepository, PlayerRepository playerRepository,
                                    MatchRepository matchRepository, RecordRepository recordRepository) {
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
        this.matchRepository = matchRepository;
        this.recordRepository = recordRepository;
    }

    public boolean didNotPlayTogether(Record recordOne, Record recordTwo) {
        if (recordOne.getFinishMin() < recordTwo.getStartMin() || recordOne.getStartMin() > recordTwo.getFinishMin()) {
            return true;
        }
        return false;
    }

    public Integer calculateMinsPlayed(Record recordA, Record recordB) {
        int result = 0;

        if (recordA.getStartMin() == recordB.getStartMin()) {
            result = Math.min(recordA.getFinishMin(), recordB.getFinishMin()) - recordA.getStartMin();

        } else if (recordA.getFinishMin() < recordB.getFinishMin()) {
            if (recordA.getStartMin() < recordB.getStartMin()) {
                result = recordA.getFinishMin() - recordB.getStartMin();
            } else {
                result = recordA.getFinishMin() - recordA.getStartMin();
            }

        } else if (recordA.getFinishMin() > recordB.getFinishMin()) {
            if (recordA.getStartMin() < recordB.getStartMin()) {
                result = recordB.getFinishMin() - recordB.getStartMin();
            } else {
                result = recordB.getFinishMin() - recordA.getStartMin();
            }
        } else if (recordA.getFinishMin() == recordB.getFinishMin()) {
            result = recordA.getFinishMin() - Math.max(recordA.getStartMin(), recordB.getStartMin());
        }
        return result;
    }

    /*I am grouping the records by match's id to make sure I am checking the records within one match,
a list of records shares the same match id; Then I can access all the records of one match by looking
 them up by the key
 */
    public Map<Integer, List<Record>> groupRecordsByMatchId() {

        List<Record> records = recordRepository.findAll();
        Map<Integer, List<Record>> recordsByMatch = new HashMap<>();

        for (Record record : records) {
            int matchId = record.getMatch().getId();
            recordsByMatch.computeIfAbsent(matchId, k -> new ArrayList<>()).add(record);
        }
        return recordsByMatch;
    }

    public HashMap<Set<Player>, HashMap<Integer, Integer>> iterateThroughRecordsByMatch() {
        Map<Integer, List<Record>> recordsByMatch = groupRecordsByMatchId();

        HashMap<Set<Player>, HashMap<Integer, Integer>> mapToCountMaxMinsPerPair = new HashMap<>();

        for (Map.Entry<Integer, List<Record>> entry : recordsByMatch.entrySet()) {
            Integer matchId = entry.getKey();
            List<Record> records = entry.getValue();
            /*
            Here I am iterating through the list of FootballRecord for one match. If two players haven't played at the same time,
            I skip them.
             */
            for (int i = 0; i < records.size() - 1; i++) {
                for (int j = i + 1; j < records.size(); j++) {
                    if (didNotPlayTogether(records.get(i), records.get(j))) {
                        continue;
                    }

                    int result = calculateMinsPlayed(records.get(i), records.get(j));
                    List<Record> currentRecords = new ArrayList<>();
                    currentRecords.add(records.get(i));
                    currentRecords.add(records.get(j));
                    // timePlayedTogetherMap.put(result, currentRecords);

                    Player playerA = records.get(i).getPlayer();
                    Player playerB = records.get(j).getPlayer();

                    Set<Player> currentPlayers = new HashSet<>(Arrays.asList(playerA, playerB));

                    if (mapToCountMaxMinsPerPair.containsKey(currentPlayers)) {
                        mapToCountMaxMinsPerPair.get(currentPlayers).put(records.get(i).getMatch().getId(), result);
                    } else {
                        HashMap<Integer, Integer> newPairMap = new HashMap<>();
                        newPairMap.put(records.get(i).getMatch().getId(), result);
                        mapToCountMaxMinsPerPair.put(currentPlayers, newPairMap);
                    }

                }
            }
        }
        return mapToCountMaxMinsPerPair;
    }

    public Set<Player> iterateMapToCountMaxMinsPerPair(HashMap<Set<Player>, HashMap<Integer, Integer>> map) {
        /*I am looking for the pair of footballers with max time played together
          since there can be multiple pairs of those who have played max amount of minutes, I really should be
          returning a map with multiple sets of pairs of players but the task doesn't require us to do that
        */
        //HashMap <Set<Player>, HashMap<Integer, Integer>> toReturn = new HashMap<>();

        Set<Player> maxTimePlayers = null;
        int maxTime = 0;

        for (Map.Entry<Set<Player>, HashMap<Integer, Integer>> entry : map.entrySet()) {
            Set<Player> playerPair = entry.getKey();
            HashMap<Integer, Integer> matchTimes = entry.getValue();

            int totalTime = 0;
            for (int time : matchTimes.values()) {
                totalTime += time;
            }

            if (totalTime > maxTime) {
                maxTime = totalTime;
                maxTimePlayers = playerPair;
            }
        }
        return maxTimePlayers;
    }


  public String getResultsForPrinting(Set<Player> maxTimePlayers, HashMap<Set<Player>, HashMap<Integer, Integer>> map) {
      if (maxTimePlayers == null || map == null || maxTimePlayers.isEmpty() || !map.containsKey(maxTimePlayers)) {
          return "No players found with matching play time.";
      }

      HashMap<Integer, Integer> matchTimes = map.get(maxTimePlayers);

      StringBuilder firstPartResult = new StringBuilder();
      StringBuilder secondPartResult = new StringBuilder();
      Iterator<Player> iterator = maxTimePlayers.iterator();
      int totalMins = 0;

      if (iterator.hasNext()) {
          Player playerA = iterator.next();
          firstPartResult.append(playerA.getId()).append(", ");
      }

      if (iterator.hasNext()) {
          Player playerB = iterator.next();
          firstPartResult.append(playerB.getId());
      }

      for (Map.Entry<Integer, Integer> entry : matchTimes.entrySet()) {
          Integer matchId = entry.getKey();
          Integer minutesPlayed = entry.getValue();
          secondPartResult.append(matchId)
                  .append(", ").append(minutesPlayed)
                  .append("\n");
          totalMins+=minutesPlayed;
      }

      firstPartResult.append(", ").append(totalMins).append("\n");

      return firstPartResult.append(secondPartResult).toString();
  }
}
