package com.paf.exercise.player.application.controllers;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.paf.exercise.WebMvcTestConfiguration;
import com.paf.exercise.player.application.models.PlayerDto;
import com.paf.exercise.player.application.services.PlayerService;
import com.paf.exercise.player.domain.entities.Player;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PlayerController.class)
@Import(WebMvcTestConfiguration.class)
@WithMockUser(
    username = "admin",
    password = "admin",
    roles = {"ADMIN"})
class PlayerControllerCreatePlayerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private PlayerService playerService;

  @Test
  void assertThatResponse_givenValidRequest_shouldReturn200() throws Exception {
    var player = new Player(1L, "Player1");
    when(playerService.save(any(PlayerDto.class))).thenReturn(player);

    mockMvc
        .perform(
            post("/bo/players")
                .contentType(APPLICATION_JSON)
                .content("{\"name\": \"Test Testsson\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is("Player1")));
  }

  @Test
  void assertThatResponse_givenBadRequest_shouldReturn400() throws Exception {
    mockMvc
        .perform(
            post("/bo/players")
                .contentType(APPLICATION_JSON)
                .content("{\"invalidField\": \"value\"}"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithAnonymousUser
  void assertThatResponse_givenUnauthorizedRequest_shouldReturn401() throws Exception {
    mockMvc
        .perform(
            post("/bo/players")
                .contentType(APPLICATION_JSON)
                .content("{\"name\": \"Test Testsson\"}"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", password = "user")
  void assertThatResponse_givenForbiddenRequest_shouldReturn403() throws Exception {
    mockMvc
        .perform(
            post("/bo/players")
                .contentType(APPLICATION_JSON)
                .content("{\"name\": \"Test Testsson\"}"))
        .andExpect(status().isForbidden());
  }

  @Test
  void assertThatResponse_givenInternalError_shouldReturn500() throws Exception {
    when(playerService.save(any(PlayerDto.class)))
        .thenThrow(new RuntimeException("Internal Server Error"));

    mockMvc
        .perform(
            post("/bo/players")
                .contentType(APPLICATION_JSON)
                .content("{\"name\": \"Test Testsson\"}"))
        .andExpect(status().isInternalServerError());
  }
}
