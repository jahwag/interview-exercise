package com.paf.exercise.tournament.application.controllers;

import com.paf.exercise.tournament.application.models.TournamentDto;
import com.paf.exercise.tournament.application.models.TournamentRegistrationDto;
import com.paf.exercise.tournament.application.services.TournamentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/bo/tournaments")
public class TournamentController {

  private final TournamentService tournamentService;

  @GetMapping
  public List<TournamentDto> getAllTournaments() {
    log.trace("Get all tournaments");
    List<TournamentDto> tournaments = tournamentService.findAll();

    log.info("Returning {} tournaments", tournaments.size());
    return tournaments;
  }

  @GetMapping("/{id}")
  public ResponseEntity<TournamentDto> getTournamentById(@PathVariable Long id) {
    log.trace("Get tournament with ID {}", id);

    return tournamentService
        .findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public TournamentDto createTournament(@Validated @RequestBody TournamentDto tournamentDTO) {
    log.trace("Create a new tournament with name {}", tournamentDTO.name());
    TournamentDto savedTournament = tournamentService.save(tournamentDTO);

    log.info("Tournament created with ID {}", savedTournament.id());
    return savedTournament;
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTournament(@PathVariable Long id) {
    log.trace("Delete tournament with ID {}", id);
    tournamentService.deleteById(id);

    log.info("Tournament with ID {} deleted", id);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{tournamentId}/players")
  public ResponseEntity<Void> registerPlayerToTournament(
      @PathVariable Long tournamentId, @RequestBody TournamentRegistrationDto registrationDto) {
    log.trace("Register playerId {} to tournament {}", registrationDto.playerId(), tournamentId);
    tournamentService.registerPlayer(tournamentId, registrationDto.playerId());

    log.info(
        "Player with ID {} registered to tournament {}", registrationDto.playerId(), tournamentId);
    return ResponseEntity.ok().build();
  }
}
