package com.paf.exercise.shared.domain.exceptions;

public class CurrencyNotFoundException extends RuntimeException {
  public CurrencyNotFoundException(String currencyCode) {
    super("Currency not found: " + currencyCode);
  }
}
