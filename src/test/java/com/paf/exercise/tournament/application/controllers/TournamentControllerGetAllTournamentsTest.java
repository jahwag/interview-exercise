package com.paf.exercise.tournament.application.controllers;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.paf.exercise.WebMvcTestConfiguration;
import com.paf.exercise.tournament.application.models.TournamentDto;
import com.paf.exercise.tournament.application.services.TournamentService;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TournamentController.class)
@Import(WebMvcTestConfiguration.class)
@WithMockUser(
    username = "admin",
    password = "admin",
    roles = {"ADMIN"})
class TournamentControllerGetAllTournamentsTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private TournamentService tournamentService;

  @Test
  void assertThatResponse_givenValidRequest_shouldReturn200() throws Exception {
    TournamentDto tournament1 =
        new TournamentDto(1L, "Tournament1", BigDecimal.valueOf(100), "USD", new HashSet<>());
    TournamentDto tournament2 =
        new TournamentDto(2L, "Tournament2", BigDecimal.valueOf(200), "EUR", new HashSet<>());
    when(tournamentService.findAll()).thenReturn(Arrays.asList(tournament1, tournament2));

    mockMvc
        .perform(get("/bo/tournaments"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].name", is("Tournament1")))
        .andExpect(jsonPath("$[1].name", is("Tournament2")));
  }

  @Test
  @WithAnonymousUser
  void assertThatResponse_givenUnauthorizedRequest_shouldReturn401() throws Exception {
    mockMvc.perform(get("/bo/tournaments")).andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", password = "user")
  void assertThatResponse_givenForbiddenRequest_shouldReturn403() throws Exception {
    mockMvc.perform(get("/bo/tournaments")).andExpect(status().isForbidden());
  }

  @Test
  void assertThatResponse_givenInternalError_shouldReturn500() throws Exception {
    when(tournamentService.findAll()).thenThrow(new RuntimeException("Internal Server Error"));

    mockMvc.perform(get("/bo/tournaments")).andExpect(status().isInternalServerError());
  }
}
