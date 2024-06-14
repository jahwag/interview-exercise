package com.paf.exercise.player.application.controllers;

import com.paf.exercise.player.application.models.PlayerDto;
import com.paf.exercise.player.application.services.PlayerService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/bo/players")
public class PlayerController {

  private final PlayerService playerService;

  @GetMapping
  public List<PlayerDto> getAllPlayers() {
    log.trace("Get all players");
    var players = playerService.findAll();
    var playerDTOs = players.stream().map(PlayerDto::of).collect(Collectors.toList());

    log.info("Returning {} players", playerDTOs.size());
    return playerDTOs;
  }

  @GetMapping("/{id}")
  public ResponseEntity<PlayerDto> getPlayerById(@PathVariable Long id) {
    log.trace("Get player with ID {}", id);

    return playerService
        .findById(id)
        .map(PlayerDto::of)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public PlayerDto createPlayer(@Validated @RequestBody PlayerDto playerDTO) {
    log.trace("Create a new player with name {}", playerDTO.name());
    var savedPlayer = playerService.save(playerDTO);

    log.info("Player created with ID {}", savedPlayer.getId());
    return PlayerDto.of(savedPlayer);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletePlayer(@PathVariable Long id) {
    log.trace("Delete player with ID {}", id);
    playerService.deleteById(id);

    log.info("Player with ID {} deleted", id);
    return ResponseEntity.noContent().build();
  }
}
