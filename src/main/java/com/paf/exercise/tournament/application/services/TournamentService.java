package com.paf.exercise.tournament.application.services;

import com.paf.exercise.shared.domain.exceptions.CurrencyNotFoundException;
import com.paf.exercise.shared.domain.exceptions.PlayerAlreadyRegisteredException;
import com.paf.exercise.shared.domain.exceptions.TournamentNameAlreadyTakenException;
import com.paf.exercise.shared.domain.exceptions.TournamentNotFoundException;
import com.paf.exercise.tournament.application.models.TournamentDto;
import com.paf.exercise.tournament.domain.entities.Tournament;
import com.paf.exercise.tournament.infrastructure.repositories.CurrencyRepository;
import com.paf.exercise.tournament.infrastructure.repositories.TournamentRepository;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TournamentService {

  private final TournamentRepository tournamentRepository;
  private final CurrencyRepository currencyRepository;

  @Transactional(readOnly = true)
  public List<TournamentDto> findAll() {
    return tournamentRepository.findAll().stream().map(TournamentDto::of).toList();
  }

  @Transactional(readOnly = true)
  public Optional<TournamentDto> findById(Long id) {
    return tournamentRepository.findById(id).map(TournamentDto::of);
  }

  @Transactional
  public TournamentDto save(TournamentDto tournamentDto) {
    var currencyCode = tournamentDto.currency();
    var currency =
        currencyRepository
            .findById(currencyCode)
            .orElseThrow(() -> new CurrencyNotFoundException(currencyCode));

    if (tournamentRepository.existsByName(tournamentDto.name())) {
      throw new TournamentNameAlreadyTakenException(tournamentDto.name());
    }

    var tournament = new Tournament();
    tournament.setName(tournamentDto.name());
    tournament.setRewardPrize(tournamentDto.rewardPrize());
    tournament.setCurrency(currency);
    tournament.setPlayerIds(null); // created empty

    return TournamentDto.of(tournamentRepository.save(tournament));
  }

  @Transactional
  public void deleteById(Long id) {
    tournamentRepository.deleteById(id);
  }

  @Transactional
  public void registerPlayer(Long tournamentId, Long playerId) {
    var tournament =
        tournamentRepository
            .findById(tournamentId)
            .orElseThrow(() -> new TournamentNotFoundException(tournamentId));

    Set<Long> playerIds =
        tournament.getPlayerIds() == null ? new HashSet<>() : tournament.getPlayerIds();
    if (tournament.getPlayerIds().contains(playerId)) {
      throw new PlayerAlreadyRegisteredException(playerId);
    }

    playerIds.add(playerId);
    tournament.setPlayerIds(playerIds);

    tournamentRepository.save(tournament);
  }
}
