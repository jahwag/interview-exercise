package com.paf.exercise.shared.application.config;

import com.paf.exercise.ExerciseApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackageClasses = ExerciseApplication.class)
@Configuration
public class JpaConfiguration {}
