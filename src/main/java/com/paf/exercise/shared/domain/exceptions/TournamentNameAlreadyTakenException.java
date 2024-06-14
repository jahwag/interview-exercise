package com.paf.exercise.shared.domain.exceptions;

public class TournamentNameAlreadyTakenException extends RuntimeException {
  public TournamentNameAlreadyTakenException(String name) {
    super("Tournament name already taken: " + name);
  }
}
