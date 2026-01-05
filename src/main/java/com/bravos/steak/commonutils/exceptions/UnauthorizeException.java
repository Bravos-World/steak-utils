package com.bravos.steak.commonutils.exceptions;

public class UnauthorizeException extends RuntimeException {

  public UnauthorizeException() {
    super();
  }

  public UnauthorizeException(String message) {
    super(message);
  }
}
