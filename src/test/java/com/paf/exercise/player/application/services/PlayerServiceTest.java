package com.paf.exercise.player.application.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.paf.exercise.player.application.models.PlayerDto;
import com.paf.exercise.player.domain.entities.Player;
import com.paf.exercise.player.infrastructure.repositories.PlayerRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

  @Mock private PlayerRepository playerRepository;

  @InjectMocks private PlayerService playerService;

  private PlayerDto playerDto;
  private Player player;

  @BeforeEach
  void setUp() {
    playerDto = new PlayerDto(0L, "Player1");
    player = new Player();
    player.setId(1L);
    player.setName("Player1");
  }

  @Test
  void findAll_shouldReturnAllPlayers() {
    when(playerRepository.findAll()).thenReturn(Arrays.asList(player));

    List<Player> result = playerService.findAll();

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("Player1", result.get(0).getName());
  }

  @Test
  void findById_shouldReturnPlayer_whenIdExists() {
    when(playerRepository.findById(1L)).thenReturn(Optional.of(player));

    Optional<Player> result = playerService.findById(1L);

    assertTrue(result.isPresent());
    assertEquals("Player1", result.get().getName());
  }

  @Test
  void findById_shouldReturnEmpty_whenIdDoesNotExist() {
    when(playerRepository.findById(1L)).thenReturn(Optional.empty());

    Optional<Player> result = playerService.findById(1L);

    assertFalse(result.isPresent());
  }

  @Test
  void save_shouldSavePlayer() {
    when(playerRepository.save(any(Player.class))).thenReturn(player);

    Player result = playerService.save(playerDto);

    assertNotNull(result);
    assertEquals("Player1", result.getName());
  }

  @Test
  void deleteById_shouldDeletePlayer() {
    doNothing().when(playerRepository).deleteById(1L);

    playerService.deleteById(1L);

    verify(playerRepository, times(1)).deleteById(1L);
  }
}
