package com.example.sirmafinalexam.repository;

import com.example.sirmafinalexam.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Integer> {
}
