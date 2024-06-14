package com.paf.exercise.shared.application.config;

import com.paf.exercise.shared.domain.exceptions.CurrencyNotFoundException;
import com.paf.exercise.shared.domain.exceptions.PlayerAlreadyRegisteredException;
import com.paf.exercise.shared.domain.exceptions.TournamentNameAlreadyTakenException;
import com.paf.exercise.shared.domain.exceptions.TournamentNotFoundException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Centralized exception handler for the Spring Boot application. Uses {@code @ControllerAdvice} to
 * intercept and handle exceptions globally, logging them for debugging and returning generic error
 * responses to maintain security and ensure a consistent user experience.
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  private final DefaultErrorAttributes defaultErrorAttributes;

  @Autowired
  public GlobalExceptionHandler(DefaultErrorAttributes defaultErrorAttributes) {
    this.defaultErrorAttributes = defaultErrorAttributes;
  }

  @ExceptionHandler(PlayerAlreadyRegisteredException.class)
  public ResponseEntity<Map<String, Object>> handlePlayerAlreadyRegisteredException(
      PlayerAlreadyRegisteredException ex, WebRequest request) {
    return handleCustomException(ex, request, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(TournamentNotFoundException.class)
  public ResponseEntity<Map<String, Object>> handleTournamentNotFoundException(
      TournamentNotFoundException ex, WebRequest request) {
    return handleCustomException(ex, request, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(CurrencyNotFoundException.class)
  public ResponseEntity<Map<String, Object>> handleCurrencyNotFoundException(
      CurrencyNotFoundException ex, WebRequest request) {
    return handleCustomException(ex, request, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(TournamentNameAlreadyTakenException.class)
  public ResponseEntity<Map<String, Object>> handleTournamentNameTakenException(
      TournamentNameAlreadyTakenException ex, WebRequest request) {
    return handleCustomException(ex, request, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> handleGenericException(
      Exception ex, WebRequest request) {
    log.error("An error occurred: ", ex);
    Map<String, Object> errorAttributes =
        defaultErrorAttributes.getErrorAttributes(request, ErrorAttributeOptions.defaults());
    errorAttributes.put("message", "An unexpected error occurred. Please try again later.");
    return new ResponseEntity<>(errorAttributes, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private ResponseEntity<Map<String, Object>> handleCustomException(
      Exception ex, WebRequest request, HttpStatus status) {
    Map<String, Object> errorAttributes =
        defaultErrorAttributes.getErrorAttributes(request, ErrorAttributeOptions.defaults());
    errorAttributes.put("message", ex.getMessage());
    return new ResponseEntity<>(errorAttributes, status);
  }
}
