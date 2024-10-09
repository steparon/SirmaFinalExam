package com.example.sirmafinalexam.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "players")
public class Player {

    @Id
    @Column(name= "player_id", unique = true)
    private int id;

    @Column(name= "team_number")
    private int teamNumber;

    @Column(name= "position")
    private String position;

    @Column(name= "full_name")
    private String fullName;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @Override
    public int hashCode() {
        return Objects.hash(id, teamNumber, position, fullName, team);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return id == player.id &&
                teamNumber == player.teamNumber &&
                Objects.equals(position, player.position) &&
                Objects.equals(fullName, player.fullName) &&
                Objects.equals(team, player.team);
    }

}
