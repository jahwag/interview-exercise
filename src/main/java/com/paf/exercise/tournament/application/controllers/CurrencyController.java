package com.paf.exercise.tournament.application.controllers;

import com.paf.exercise.tournament.application.services.CurrencyService;
import com.paf.exercise.tournament.domain.entities.Currency;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/bo/currencies")
public class CurrencyController {

  private final CurrencyService currencyService;

  @Transactional(readOnly = true)
  @GetMapping
  public List<String> getAllCurrencies() {
    log.trace("Get all currencies");
    List<Currency> currencies = currencyService.findAllCurrencies();

    log.info("Returning {} currencies", currencies.size());
    return currencies.stream().map(Currency::getCode).toList();
  }
}
