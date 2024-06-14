package com.paf.exercise.player.application.controllers;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.paf.exercise.WebMvcTestConfiguration;
import com.paf.exercise.player.application.services.PlayerService;
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
class PlayerControllerDeletePlayerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private PlayerService playerService;

  @Test
  void assertThatResponse_givenValidId_shouldReturn204() throws Exception {
    doNothing().when(playerService).deleteById(anyLong());

    mockMvc.perform(delete("/bo/players/{id}", 1L)).andExpect(status().isNoContent());
  }

  @Test
  void assertThatResponse_givenBadRequest_shouldReturn400() throws Exception {
    mockMvc.perform(delete("/bo/players/{id}", "invalidId")).andExpect(status().isBadRequest());
  }

  @Test
  @WithAnonymousUser
  void assertThatResponse_givenUnauthorizedRequest_shouldReturn401() throws Exception {
    mockMvc.perform(delete("/bo/players/{id}", 1L)).andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", password = "user")
  void assertThatResponse_givenForbiddenRequest_shouldReturn403() throws Exception {
    mockMvc.perform(delete("/bo/players/{id}", 1L)).andExpect(status().isForbidden());
  }

  @Test
  void assertThatResponse_givenInternalError_shouldReturn500() throws Exception {
    doThrow(new RuntimeException("Internal Server Error"))
        .when(playerService)
        .deleteById(anyLong());

    mockMvc.perform(delete("/bo/players/{id}", 1L)).andExpect(status().isInternalServerError());
  }
}
