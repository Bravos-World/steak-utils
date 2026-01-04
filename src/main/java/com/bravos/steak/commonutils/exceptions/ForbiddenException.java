package com.bravos.steak.commonutils.exceptions;

public class ForbiddenException extends RuntimeException {

  public ForbiddenException() {
    super();
  }

  public ForbiddenException(String message) {
    super(message);
  }
}
