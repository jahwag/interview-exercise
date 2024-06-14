package com.paf.exercise.tournament.application.controllers;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.paf.exercise.WebMvcTestConfiguration;
import com.paf.exercise.shared.domain.exceptions.PlayerAlreadyRegisteredException;
import com.paf.exercise.tournament.application.services.TournamentService;
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
class TournamentControllerRegisterPlayerToTournamentTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private TournamentService tournamentService;

  @Test
  void assertThatResponse_givenValidRequest_shouldReturn200() throws Exception {
    doNothing().when(tournamentService).registerPlayer(anyLong(), anyLong());

    mockMvc
        .perform(
            post("/bo/tournaments/{tournamentId}/players", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"playerId\": 1}"))
        .andExpect(status().isOk());
  }

  @Test
  void assertThatResponse_givenBadRequest_shouldReturn409() throws Exception {
    doThrow(new PlayerAlreadyRegisteredException(1L))
        .when(tournamentService)
        .registerPlayer(anyLong(), anyLong());

    mockMvc
        .perform(
            post("/bo/tournaments/{tournamentId}/players", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"playerId\": 1}"))
        .andExpect(status().isConflict());
  }

  @Test
  @WithAnonymousUser
  void assertThatResponse_givenUnauthorizedRequest_shouldReturn401() throws Exception {
    mockMvc
        .perform(
            post("/bo/tournaments/{tournamentId}/players", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"playerId\": 1}"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", password = "user")
  void assertThatResponse_givenForbiddenRequest_shouldReturn403() throws Exception {
    mockMvc
        .perform(
            post("/bo/tournaments/{tournamentId}/players", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"playerId\": 1}"))
        .andExpect(status().isForbidden());
  }

  @Test
  void assertThatResponse_givenInternalError_shouldReturn500() throws Exception {
    doThrow(new RuntimeException("Internal Server Error"))
        .when(tournamentService)
        .registerPlayer(anyLong(), anyLong());

    mockMvc
        .perform(
            post("/bo/tournaments/{tournamentId}/players", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"playerId\": 1}"))
        .andExpect(status().isInternalServerError());
  }
}
