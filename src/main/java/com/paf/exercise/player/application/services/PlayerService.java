package com.paf.exercise.player.application.services;

import com.paf.exercise.player.application.models.PlayerDto;
import com.paf.exercise.player.domain.entities.Player;
import com.paf.exercise.player.infrastructure.repositories.PlayerRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerService {

  private final PlayerRepository playerRepository;

  public List<Player> findAll() {
    return playerRepository.findAll();
  }

  public Optional<Player> findById(Long id) {
    return playerRepository.findById(id);
  }

  public Player save(PlayerDto playerDto) {
    Player player = new Player();
    player.setName(playerDto.name());

    return playerRepository.save(player);
  }

  public void deleteById(Long id) {
    playerRepository.deleteById(id);
  }
}
