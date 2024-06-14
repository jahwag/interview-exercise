package com.paf.exercise.tournament.domain.entities;

import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Tournament {
  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  private String name;
  private BigDecimal rewardPrize;

  @ManyToOne
  @JoinColumn(name = "currency")
  private Currency currency;

  @ElementCollection(fetch = EAGER)
  @CollectionTable(name = "tournament_player", joinColumns = @JoinColumn(name = "tournament_id"))
  @Column(name = "player_id")
  private Set<Long> playerIds;
}
