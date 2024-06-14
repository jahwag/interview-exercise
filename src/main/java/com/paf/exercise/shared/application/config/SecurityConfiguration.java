package com.paf.exercise.shared.application.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

  private static final String ADMIN = "ADMIN";

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable) // Disable CSRF protection
        .sessionManagement(
            session -> session.sessionCreationPolicy(STATELESS)) // Disable session management
        .authorizeHttpRequests(auth -> auth.requestMatchers("/", "/swagger-ui.html").permitAll())
        .authorizeHttpRequests(auth -> auth.requestMatchers("/bo/**").hasRole(ADMIN))
        .authorizeHttpRequests(auth -> auth.requestMatchers("/**").authenticated())
        .httpBasic(Customizer.withDefaults());

    return http.build();
  }

  @Bean
  public UserDetailsService userDetailsService(DataSource dataSource) {
    return new JdbcUserDetailsManager(dataSource);
  }
}
