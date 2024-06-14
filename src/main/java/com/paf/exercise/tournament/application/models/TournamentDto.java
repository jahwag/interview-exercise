package com.paf.exercise.tournament.application.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.paf.exercise.tournament.domain.entities.Currency;
import com.paf.exercise.tournament.domain.entities.Tournament;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TournamentDto(
    @Schema(defaultValue = "-1") Long id,
    @NotBlank @Schema(defaultValue = "Big Tournament") String name,
    @Min(1) @Max(1_000_000_000L) @Schema(defaultValue = "10") BigDecimal rewardPrize,
    @Schema(defaultValue = "EUR") String currency,
    Set<Long> playerIds) {

  public static TournamentDto of(Tournament tournament) {
    return new TournamentDto(
        tournament.getId(),
        tournament.getName(),
        tournament.getRewardPrize(),
        tournament.getCurrency().getCode(),
        tournament.getPlayerIds());
  }

  public Tournament toTournament() {
    Tournament tournament = new Tournament();
    tournament.setName(name);
    tournament.setRewardPrize(rewardPrize);
    tournament.setCurrency(new Currency(currency));
    tournament.setPlayerIds(playerIds);
    return tournament;
  }
}
