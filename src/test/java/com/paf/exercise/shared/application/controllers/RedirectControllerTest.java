package com.paf.exercise.shared.application.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.paf.exercise.WebMvcTestConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(RedirectController.class)
@Import(WebMvcTestConfiguration.class)
class RedirectControllerTest {

  @Autowired private MockMvc mockMvc;

  @Test
  @WithMockUser(
      username = "admin",
      password = "admin",
      roles = {"ADMIN"})
  void assertThatRedirectToSwaggerUI_givenRequestToRoot_shouldReturn302() throws Exception {
    mockMvc
        .perform(get("/"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/swagger-ui.html"));
  }

  @Test
  @WithAnonymousUser
  void assertThatResponse_givenAnonymousRequest_shouldReturn302() throws Exception {
    mockMvc
        .perform(get("/"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/swagger-ui.html"));
  }

  @Test
  @WithMockUser(username = "user", password = "user")
  void assertThatResponse_givenUserRole_shouldReturn302() throws Exception {
    mockMvc
        .perform(get("/"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/swagger-ui.html"));
  }
}
