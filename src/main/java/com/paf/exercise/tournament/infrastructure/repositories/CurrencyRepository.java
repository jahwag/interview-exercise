package com.paf.exercise.tournament.infrastructure.repositories;

import com.paf.exercise.tournament.domain.entities.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency, String> {}
