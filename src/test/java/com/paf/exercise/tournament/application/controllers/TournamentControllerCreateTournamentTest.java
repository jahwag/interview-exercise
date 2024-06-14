package com.paf.exercise.tournament.application.controllers;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.paf.exercise.WebMvcTestConfiguration;
import com.paf.exercise.tournament.application.models.TournamentDto;
import com.paf.exercise.tournament.application.services.TournamentService;
import java.math.BigDecimal;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TournamentController.class)
@Import(WebMvcTestConfiguration.class)
@WithMockUser(
    username = "admin",
    password = "admin",
    roles = {"ADMIN"})
class TournamentControllerCreateTournamentTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private TournamentService tournamentService;

  @Test
  void assertThatResponse_givenValidRequest_shouldReturn200() throws Exception {
    TournamentDto tournament =
        new TournamentDto(1L, "Tournament1", new BigDecimal("10"), "EUR", Set.of(1L, 2L, 3L));
    when(tournamentService.save(any(TournamentDto.class))).thenReturn(tournament);

    mockMvc
        .perform(
            post("/bo/tournaments")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Tournament1\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is("Tournament1")));
  }

  @Test
  void assertThatResponse_givenBadRequest_shouldReturn400() throws Exception {
    mockMvc
        .perform(
            post("/bo/tournaments")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"invalidField\": \"value\"}"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithAnonymousUser
  void assertThatResponse_givenUnauthorizedRequest_shouldReturn401() throws Exception {
    mockMvc
        .perform(
            post("/bo/tournaments")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Tournament1\"}"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", password = "user")
  void assertThatResponse_givenForbiddenRequest_shouldReturn403() throws Exception {
    mockMvc
        .perform(
            post("/bo/tournaments")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Tournament1\"}"))
        .andExpect(status().isForbidden());
  }

  @Test
  void assertThatResponse_givenInternalError_shouldReturn500() throws Exception {
    when(tournamentService.save(any(TournamentDto.class)))
        .thenThrow(new RuntimeException("Internal Server Error"));

    mockMvc
        .perform(
            post("/bo/tournaments")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Tournament1\"}"))
        .andExpect(status().isInternalServerError());
  }
}
