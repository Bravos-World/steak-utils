package com.bravos.steak.commonutils.exceptions.netty.handler;

import com.bravos.steak.commonutils.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class ReactiveExceptionHandler {

  @ExceptionHandler(Exception.class)
  public Mono<ResponseEntity<ErrorResponse>> handleGenericException(Exception exception) {
    String message = exception.getMessage() != null ? exception.getMessage() : "Unknown error";
    ErrorResponse errorResponse = new ErrorResponse("An unexpected error occurred: " + message);
    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse));
  }

  @ExceptionHandler(NotFoundException.class)
  public Mono<ResponseEntity<ErrorResponse>> handleNotFoundException(NotFoundException exception) {
    String message = exception.getMessage() != null ? exception.getMessage() : "Resource not found";
    ErrorResponse errorResponse = new ErrorResponse(message);
    return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse));
  }

  @ExceptionHandler(BadRequestException.class)
  public Mono<ResponseEntity<ErrorResponse>> handleBadRequestException(BadRequestException exception) {
    String message = exception.getMessage() != null ? exception.getMessage() : "Bad request";
    ErrorResponse errorResponse = new ErrorResponse(message);
    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse));
  }

  @ExceptionHandler(ConflictDataException.class)
  public Mono<ResponseEntity<ErrorResponse>> handleConflictDataException(ConflictDataException exception) {
    String message = exception.getMessage() != null ? exception.getMessage() : "Conflict data";
    ErrorResponse errorResponse = new ErrorResponse(message);
    return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse));
  }

  @ExceptionHandler(ForbiddenException.class)
  public Mono<ResponseEntity<ErrorResponse>> handleForbiddenException(ForbiddenException exception) {
    String message = exception.getMessage() != null ? exception.getMessage() : "Forbidden";
    ErrorResponse errorResponse = new ErrorResponse(message);
    return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse));
  }

  @ExceptionHandler(UnauthorizeException.class)
  public Mono<ResponseEntity<ErrorResponse>> handleUnauthorizeException(UnauthorizeException exception) {
    String message = exception.getMessage() != null ? exception.getMessage() : "Unauthorized";
    ErrorResponse errorResponse = new ErrorResponse(message);
    return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse));
  }

  @ExceptionHandler(TooManyRequestException.class)
  public Mono<ResponseEntity<ErrorResponse>> handleTooManyRequestException(TooManyRequestException exception) {
    String message = exception.getMessage() != null ? exception.getMessage() : "Too many requests";
    ErrorResponse errorResponse = new ErrorResponse(message);
    return Mono.just(ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(errorResponse));
  }

  @ExceptionHandler(ServerWebInputException.class)
  public Mono<ResponseEntity<ErrorResponse>> handleTypeMismatch(ServerWebInputException ex) {
    String errorMessage = "Invalid request input: " + ex.getReason();
    ErrorResponse errorResponse = new ErrorResponse(errorMessage);
    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse));
  }

  @ExceptionHandler(WebExchangeBindException.class)
  public Mono<ResponseEntity<ErrorResponse>> handleWebExchangeBindException(WebExchangeBindException ex) {
    Map<String,String> errors = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .collect(Collectors.toMap(
            FieldError::getField,
            fieldError -> fieldError.getDefaultMessage() != null ? fieldError.getDefaultMessage() : "invalid data",
            (existing, replacement) -> existing + " ; " + replacement
        ));
    ErrorResponse errorResponse = new ValidateErrorReponse("Invalid request parameters", errors);
    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse));
  }

}
