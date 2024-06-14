package com.paf.exercise.tournament.application.controllers;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.paf.exercise.WebMvcTestConfiguration;
import com.paf.exercise.tournament.application.services.CurrencyService;
import com.paf.exercise.tournament.domain.entities.Currency;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CurrencyController.class)
@Import(WebMvcTestConfiguration.class)
@WithMockUser(
    username = "admin",
    password = "admin",
    roles = {"ADMIN"})
class CurrencyControllerGetAllCurrenciesTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private CurrencyService currencyService;

  @Test
  void assertThatResponse_givenValidRequest_shouldReturn200() throws Exception {
    Currency currency1 = new Currency("USD");
    Currency currency2 = new Currency("EUR");
    when(currencyService.findAllCurrencies()).thenReturn(Arrays.asList(currency1, currency2));

    mockMvc
        .perform(get("/bo/currencies"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0]", is("USD")))
        .andExpect(jsonPath("$[1]", is("EUR")));
  }

  @Test
  @WithAnonymousUser
  void assertThatResponse_givenUnauthorizedRequest_shouldReturn401() throws Exception {
    mockMvc.perform(get("/bo/currencies")).andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", password = "user")
  void assertThatResponse_givenForbiddenRequest_shouldReturn403() throws Exception {
    mockMvc.perform(get("/bo/currencies")).andExpect(status().isForbidden());
  }

  @Test
  void assertThatResponse_givenInternalError_shouldReturn500() throws Exception {
    when(currencyService.findAllCurrencies())
        .thenThrow(new RuntimeException("Internal Server Error"));

    mockMvc.perform(get("/bo/currencies")).andExpect(status().isInternalServerError());
  }
}
