package com.paf.exercise.tournament.application.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.paf.exercise.tournament.domain.entities.Currency;
import com.paf.exercise.tournament.infrastructure.repositories.CurrencyRepository;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest {

  @Mock private CurrencyRepository currencyRepository;

  @InjectMocks private CurrencyService currencyService;

  private List<Currency> currencies;

  @BeforeEach
  void setUp() {
    Currency currency1 = new Currency("USD");
    Currency currency2 = new Currency("EUR");
    currencies = Arrays.asList(currency1, currency2);
  }

  @Test
  void findAllCurrencies_shouldReturnAllCurrencies() {
    when(currencyRepository.findAll()).thenReturn(currencies);

    List<Currency> result = currencyService.findAllCurrencies();

    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals("USD", result.get(0).getCode());
    assertEquals("EUR", result.get(1).getCode());
  }

  @Test
  void findAllCurrencies_shouldReturnEmptyList_whenNoCurrenciesFound() {
    when(currencyRepository.findAll()).thenReturn(Arrays.asList());

    List<Currency> result = currencyService.findAllCurrencies();

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }
}
