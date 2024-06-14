package com.paf.exercise.player.application.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.paf.exercise.player.domain.entities.Player;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PlayerDto(
    Long id,
    @NotBlank
        @Pattern(regexp = "^[A-Z][a-z]*(\\s[A-Z][a-z]*)*$")
        @Schema(defaultValue = "Jahziah Wagner")
        String name) {
  public static PlayerDto of(Player player) {
    return new PlayerDto(player.getId(), player.getName());
  }
}
