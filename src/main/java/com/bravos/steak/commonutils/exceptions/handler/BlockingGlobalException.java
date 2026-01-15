package com.bravos.steak.commonutils.exceptions.handler;

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

import static com.bravos.steak.commonutils.exceptions.ExceptionStatic.*;

@ControllerAdvice
public class BlockingGlobalException {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(Exception exception) {
    String message = exception.getMessage() != null ? exception.getMessage() : UNKNOWN_ERROR;
    ErrorResponse errorResponse = new ErrorResponse(message, INTERNAL_SERVER_ERROR_DEFAULT_CODE);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException exception) {
    String message = exception.getMessage() != null ? exception.getMessage() : RESOURCE_NOT_FOUND;
    ErrorResponse errorResponse = new ErrorResponse(message, exception.getCode() != null ? exception.getCode() : NOT_FOUND_DEFAULT_CODE);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException exception) {
    String message = exception.getMessage() != null ? exception.getMessage() : BAD_REQUEST;
    ErrorResponse errorResponse = new ErrorResponse(message, exception.getCode() != null ? exception.getCode() : BAD_REQUEST_DEFAULT_CODE);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(ConflictDataException.class)
  public ResponseEntity<ErrorResponse> handleConflictDataException(ConflictDataException exception) {
    String message = exception.getMessage() != null ? exception.getMessage() : CONFLICT_DATA;
    ErrorResponse errorResponse = new ErrorResponse(message, exception.getCode() != null ? exception.getCode() : CONFLICT_DATA_DEFAULT_CODE);
    return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
  }

  @ExceptionHandler(ForbiddenException.class)
  public ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException exception) {
    String message = exception.getMessage() != null ? exception.getMessage() : FORBIDDEN;
    ErrorResponse errorResponse = new ErrorResponse(message, exception.getCode() != null ? exception.getCode() : FORBIDDEN_DEFAULT_CODE);
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
  }

  @ExceptionHandler(UnauthorizeException.class)
  public ResponseEntity<ErrorResponse> handleUnauthorizeException(UnauthorizeException exception) {
    String message = exception.getMessage() != null ? exception.getMessage() : UNAUTHORIZED;
    ErrorResponse errorResponse = new ErrorResponse(message, exception.getCode() != null ? exception.getCode() : UNAUTHORIZED_DEFAULT_CODE);
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
  }

  @ExceptionHandler(TooManyRequestException.class)
  public ResponseEntity<ErrorResponse> handleTooManyRequestException(TooManyRequestException exception) {
    String message = exception.getMessage() != null ? exception.getMessage() : TOO_MANY_REQUESTS;
    ErrorResponse errorResponse = new ErrorResponse(message, exception.getCode() != null ? exception.getCode() : TOO_MANY_REQUESTS_DEFAULT_CODE);
    return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(errorResponse);
  }

  @ExceptionHandler(InternalErrorException.class)
  public ResponseEntity<ErrorResponse> handleInternalErrorException(InternalErrorException exception) {
    String message = exception.getMessage() != null ? exception.getMessage() : UNKNOWN_ERROR;
    ErrorResponse errorResponse = new ErrorResponse(message, exception.getCode() != null ? exception.getCode() : INTERNAL_SERVER_ERROR_DEFAULT_CODE);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
    String requiredType = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown type";
    String errorMessage = String.format("Invalid value '%s' for parameter '%s'. Expected type: %s", ex.getValue(), ex.getName(), requiredType);
    ErrorResponse errorResponse = new ErrorResponse(errorMessage, INVALID_PARAMETER_DEFAULT_CODE);
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
    ErrorResponse errorResponse = new ValidateErrorReponse("Invalid request parameters", INVALID_PARAMETER_DEFAULT_CODE, errors);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(SecurityException.class)
  public ResponseEntity<ErrorResponse> handleSecurityException(SecurityException exception) {
    String message = exception.getMessage() != null ? exception.getMessage() : UNAUTHORIZED;
    ErrorResponse errorResponse = new ErrorResponse(message, UNAUTHORIZED_DEFAULT_CODE);
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
  }

}
