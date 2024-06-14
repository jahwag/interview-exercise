package com.paf.exercise.tournament.application.services;

import com.paf.exercise.tournament.domain.entities.Currency;
import com.paf.exercise.tournament.infrastructure.repositories.CurrencyRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CurrencyService {

  private final CurrencyRepository currencyRepository;

  @Transactional(readOnly = true)
  public List<Currency> findAllCurrencies() {
    return currencyRepository.findAll();
  }
}
