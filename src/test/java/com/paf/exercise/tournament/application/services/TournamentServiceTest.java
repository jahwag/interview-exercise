package com.paf.exercise.tournament.application.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.paf.exercise.shared.domain.exceptions.CurrencyNotFoundException;
import com.paf.exercise.shared.domain.exceptions.PlayerAlreadyRegisteredException;
import com.paf.exercise.shared.domain.exceptions.TournamentNameAlreadyTakenException;
import com.paf.exercise.shared.domain.exceptions.TournamentNotFoundException;
import com.paf.exercise.tournament.application.models.TournamentDto;
import com.paf.exercise.tournament.domain.entities.Currency;
import com.paf.exercise.tournament.domain.entities.Tournament;
import com.paf.exercise.tournament.infrastructure.repositories.CurrencyRepository;
import com.paf.exercise.tournament.infrastructure.repositories.TournamentRepository;
import java.math.BigDecimal;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TournamentServiceTest {

  @Mock private TournamentRepository tournamentRepository;

  @Mock private CurrencyRepository currencyRepository;

  @InjectMocks private TournamentService tournamentService;

  private TournamentDto tournamentDto;
  private Tournament tournament;

  @BeforeEach
  void setUp() {
    tournamentDto =
        new TournamentDto(1L, "Tournament1", BigDecimal.valueOf(100), "USD", new HashSet<>());
    tournament = new Tournament();
    tournament.setId(1L);
    tournament.setName("Tournament1");
    tournament.setRewardPrize(BigDecimal.valueOf(100));
    tournament.setCurrency(new Currency("USD"));
    tournament.setPlayerIds(new HashSet<>());
  }

  @Test
  void findAll_shouldReturnAllTournaments() {
    when(tournamentRepository.findAll()).thenReturn(Arrays.asList(tournament));

    List<TournamentDto> result = tournamentService.findAll();

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("Tournament1", result.get(0).name());
  }

  @Test
  void findById_shouldReturnTournament_whenIdExists() {
    when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));

    Optional<TournamentDto> result = tournamentService.findById(1L);

    assertTrue(result.isPresent());
    assertEquals("Tournament1", result.get().name());
  }

  @Test
  void findById_shouldReturnEmpty_whenIdDoesNotExist() {
    when(tournamentRepository.findById(1L)).thenReturn(Optional.empty());

    Optional<TournamentDto> result = tournamentService.findById(1L);

    assertFalse(result.isPresent());
  }

  @Test
  void save_shouldSaveTournament_whenNameIsUnique() {
    when(currencyRepository.findById("USD")).thenReturn(Optional.of(new Currency("USD")));
    when(tournamentRepository.existsByName("Tournament1")).thenReturn(false);
    when(tournamentRepository.save(any(Tournament.class))).thenReturn(tournament);

    TournamentDto result = tournamentService.save(tournamentDto);

    assertNotNull(result);
    assertEquals("Tournament1", result.name());
  }

  @Test
  void save_shouldThrowException_whenNameIsTaken() {
    when(currencyRepository.findById("USD")).thenReturn(Optional.of(new Currency("USD")));
    when(tournamentRepository.existsByName("Tournament1")).thenReturn(true);

    assertThrows(
        TournamentNameAlreadyTakenException.class, () -> tournamentService.save(tournamentDto));
  }

  @Test
  void save_shouldThrowException_whenCurrencyNotFound() {
    when(currencyRepository.findById("USD")).thenReturn(Optional.empty());

    assertThrows(CurrencyNotFoundException.class, () -> tournamentService.save(tournamentDto));
  }

  @Test
  void deleteById_shouldDeleteTournament() {
    doNothing().when(tournamentRepository).deleteById(1L);

    tournamentService.deleteById(1L);

    verify(tournamentRepository, times(1)).deleteById(1L);
  }

  @Test
  void registerPlayer_shouldRegisterPlayer_whenPlayerNotAlreadyRegistered() {
    when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
    when(tournamentRepository.save(any(Tournament.class))).thenReturn(tournament);

    tournamentService.registerPlayer(1L, 1L);

    assertTrue(tournament.getPlayerIds().contains(1L));
    verify(tournamentRepository, times(1)).save(tournament);
  }

  @Test
  void registerPlayer_shouldThrowException_whenPlayerAlreadyRegistered() {
    tournament.getPlayerIds().add(1L);
    when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));

    assertThrows(
        PlayerAlreadyRegisteredException.class, () -> tournamentService.registerPlayer(1L, 1L));
  }

  @Test
  void registerPlayer_shouldThrowException_whenTournamentNotFound() {
    when(tournamentRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(TournamentNotFoundException.class, () -> tournamentService.registerPlayer(1L, 1L));
  }
}
