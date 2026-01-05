package com.bravos.steak.commonutils.exceptions.tomcat.handler;

import com.bravos.steak.commonutils.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class BlockingGlobalException {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(Exception exception) {
    String message = exception.getMessage() != null ? exception.getMessage() : "Unknown error";
    ErrorResponse errorResponse = new ErrorResponse("An unexpected error occurred: " + message);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException exception) {
    String message = exception.getMessage() != null ? exception.getMessage() : "Resource not found";
    ErrorResponse errorResponse = new ErrorResponse(message);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException exception) {
    String message = exception.getMessage() != null ? exception.getMessage() : "Bad request";
    ErrorResponse errorResponse = new ErrorResponse(message);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(ConflictDataException.class)
  public ResponseEntity<ErrorResponse> handleConflictDataException(ConflictDataException exception) {
    String message = exception.getMessage() != null ? exception.getMessage() : "Conflict data";
    ErrorResponse errorResponse = new ErrorResponse(message);
    return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
  }

  @ExceptionHandler(ForbiddenException.class)
  public ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException exception) {
    String message = exception.getMessage() != null ? exception.getMessage() : "Forbidden";
    ErrorResponse errorResponse = new ErrorResponse(message);
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
  }

  @ExceptionHandler(UnauthorizeException.class)
  public ResponseEntity<ErrorResponse> handleUnauthorizeException(UnauthorizeException exception) {
    String message = exception.getMessage() != null ? exception.getMessage() : "Unauthorized";
    ErrorResponse errorResponse = new ErrorResponse(message);
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
  }

  @ExceptionHandler(TooManyRequestException.class)
  public ResponseEntity<ErrorResponse> handleTooManyRequestException(TooManyRequestException exception) {
    String message = exception.getMessage() != null ? exception.getMessage() : "Too many requests";
    ErrorResponse errorResponse = new ErrorResponse(message);
    return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(errorResponse);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
    String requiredType = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown type";
    String errorMessage = String.format("Invalid value '%s' for parameter '%s'. Expected type: %s",
        ex.getValue(), ex.getName(), requiredType);
    ErrorResponse errorResponse = new ErrorResponse(errorMessage);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
    Map<String, String> errors = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .collect(Collectors.toMap(
            FieldError::getField,
            fieldError -> fieldError.getDefaultMessage() != null ? fieldError.getDefaultMessage() : "invalid data",
            (existing, replacement) -> existing + " ; " + replacement
        ));
    ErrorResponse errorResponse = new ValidateErrorReponse("Invalid request parameters", errors);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

}
