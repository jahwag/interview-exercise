package com.paf.exercise.shared.domain.exceptions;

public class PlayerAlreadyRegisteredException extends RuntimeException {
  public PlayerAlreadyRegisteredException(Long playerId) {
    super("Player with id " + playerId + " already registered");
  }
}
