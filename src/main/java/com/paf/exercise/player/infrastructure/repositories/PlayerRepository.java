package com.paf.exercise.player.infrastructure.repositories;

import com.paf.exercise.player.domain.entities.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {}
