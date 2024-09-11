package com.example.sirmafinalexam.model;

import jakarta.persistence.*;
import lombok.*;

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
}
