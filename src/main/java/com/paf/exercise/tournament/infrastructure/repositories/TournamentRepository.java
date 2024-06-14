package com.paf.exercise.tournament.infrastructure.repositories;

import com.paf.exercise.tournament.domain.entities.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TournamentRepository extends JpaRepository<Tournament, Long> {

  boolean existsByName(String name);
}
