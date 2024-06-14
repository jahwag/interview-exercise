package com.paf.exercise;

import com.paf.exercise.shared.application.config.SecurityConfiguration;
import javax.sql.DataSource;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@TestConfiguration
@Import(SecurityConfiguration.class)
public class WebMvcTestConfiguration {

  @Bean
  public DataSource dataSource() {
    return Mockito.mock(DataSource.class);
  }
}
