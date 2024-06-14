package com.paf.exercise.shared.domain.exceptions;

public class TournamentNotFoundException extends RuntimeException {
  public TournamentNotFoundException(Long tournamentId) {
    super("Tournament with id " + tournamentId + " not found");
  }
}
