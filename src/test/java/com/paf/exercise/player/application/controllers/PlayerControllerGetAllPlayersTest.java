package com.paf.exercise.player.application.controllers;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.paf.exercise.WebMvcTestConfiguration;
import com.paf.exercise.player.application.services.PlayerService;
import com.paf.exercise.player.domain.entities.Player;
import java.util.Arrays;
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
class PlayerControllerGetAllPlayersTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private PlayerService playerService;

  @Test
  void assertThatResponse_givenValidRequest_shouldReturn200() throws Exception {
    when(playerService.findAll())
        .thenReturn(Arrays.asList(new Player(1L, "Player1"), new Player(2L, "Player2")));

    mockMvc
        .perform(get("/bo/players"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].name", is("Player1")))
        .andExpect(jsonPath("$[1].name", is("Player2")));
  }

  @Test
  @WithAnonymousUser
  void assertThatResponse_givenUnauthorizedRequest_shouldReturn401() throws Exception {
    mockMvc.perform(get("/bo/players")).andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", password = "user")
  void assertThatResponse_givenForbiddenRequest_shouldReturn403() throws Exception {
    mockMvc.perform(get("/bo/players")).andExpect(status().isForbidden());
  }

  @Test
  void assertThatResponse_givenInternalError_shouldReturn500() throws Exception {
    when(playerService.findAll()).thenThrow(new RuntimeException("Internal Server Error"));

    // Perform the GET request
    mockMvc.perform(get("/bo/players")).andExpect(status().isInternalServerError());
  }
}
