package com.bravos.steak.commonutils.exceptions;

public class TooManyRequestException extends RuntimeException {

  public TooManyRequestException() {
    super();
  }

  public TooManyRequestException(String message) {
    super(message);
  }
}
