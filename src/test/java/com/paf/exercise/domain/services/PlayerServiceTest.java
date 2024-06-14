package com.paf.exercise.domain.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.paf.exercise.player.application.models.PlayerDto;
import com.paf.exercise.player.application.services.PlayerService;
import com.paf.exercise.player.domain.entities.Player;
import com.paf.exercise.player.infrastructure.repositories.PlayerRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceTest {

  @Mock private PlayerRepository playerRepository;

  @InjectMocks private PlayerService playerService;

  @Test
  void testFindAll() {
    var player1 = new Player();
    player1.setName("Player1");

    var player2 = new Player();
    player2.setName("Player2");

    var players = Arrays.asList(player1, player2);
    when(playerRepository.findAll()).thenReturn(players);

    List<Player> result = playerService.findAll();
    assertEquals(2, result.size());
    assertEquals("Player1", result.get(0).getName());
    assertEquals("Player2", result.get(1).getName());
  }

  @Test
  void testFindById() {
    var player = new Player();
    player.setName("Player1");

    when(playerRepository.findById(1L)).thenReturn(Optional.of(player));

    Optional<Player> result = playerService.findById(1L);
    assertTrue(result.isPresent());
    assertEquals("Player1", result.get().getName());
  }

  @Test
  void testSave() {
    var playerDto = new PlayerDto(0L, "Player1");
    var player = new Player();
    player.setName(playerDto.name());

    when(playerRepository.save(any(Player.class))).thenReturn(player);

    var result = playerService.save(playerDto);
    assertNotNull(result);
    assertEquals("Player1", result.getName());
  }

  @Test
  void testDeleteById() {
    doNothing().when(playerRepository).deleteById(1L);
    playerService.deleteById(1L);
    verify(playerRepository, times(1)).deleteById(1L);
  }
}
