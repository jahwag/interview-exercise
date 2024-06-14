package com.paf.exercise.shared.application.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class RedirectController {

  @GetMapping("/")
  public RedirectView redirectToAnotherUrl() {
    String redirectUrl = "/swagger-ui.html";
    return new RedirectView(redirectUrl);
  }
}
