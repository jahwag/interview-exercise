package com.paf.exercise.tournament.application.controllers;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.paf.exercise.WebMvcTestConfiguration;
import com.paf.exercise.tournament.application.models.TournamentDto;
import com.paf.exercise.tournament.application.services.TournamentService;
import java.math.BigDecimal;
import java.util.Optional;
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
class TournamentControllerGetTournamentByIdTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private TournamentService tournamentService;

  @Test
  void assertThatResponse_givenValidId_shouldReturn200() throws Exception {
    TournamentDto tournament =
        new TournamentDto(1L, "Tournament1", BigDecimal.valueOf(100), "USD", null);
    when(tournamentService.findById(anyLong())).thenReturn(Optional.of(tournament));

    mockMvc
        .perform(get("/bo/tournaments/{id}", 1L))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is("Tournament1")));
  }

  @Test
  void assertThatResponse_givenInvalidId_shouldReturn404() throws Exception {
    when(tournamentService.findById(anyLong())).thenReturn(Optional.empty());

    mockMvc.perform(get("/bo/tournaments/{id}", 1L)).andExpect(status().isNotFound());
  }

  @Test
  void assertThatResponse_givenBadRequest_shouldReturn400() throws Exception {
    mockMvc.perform(get("/bo/tournaments/{id}", "invalidId")).andExpect(status().isBadRequest());
  }

  @Test
  @WithAnonymousUser
  void assertThatResponse_givenUnauthorizedRequest_shouldReturn401() throws Exception {
    mockMvc.perform(get("/bo/tournaments/{id}", 1L)).andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", password = "user")
  void assertThatResponse_givenForbiddenRequest_shouldReturn403() throws Exception {
    mockMvc.perform(get("/bo/tournaments/{id}", 1L)).andExpect(status().isForbidden());
  }

  @Test
  void assertThatResponse_givenInternalError_shouldReturn500() throws Exception {
    when(tournamentService.findById(anyLong()))
        .thenThrow(new RuntimeException("Internal Server Error"));

    mockMvc.perform(get("/bo/tournaments/{id}", 1L)).andExpect(status().isInternalServerError());
  }
}
