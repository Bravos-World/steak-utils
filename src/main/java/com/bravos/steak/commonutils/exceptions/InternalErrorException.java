package com.bravos.steak.commonutils.exceptions;

public class InternalErrorException extends RuntimeException {

  public InternalErrorException() {
    super();
  }

  public InternalErrorException(String message) {
    super(message);
  }
}
