package com.bravos.steak.commonutils.exceptions;

import lombok.Getter;

public class InternalErrorException extends RuntimeException {

  @Getter
  private String code;

  public InternalErrorException() {
    super();
  }

  public InternalErrorException(String message) {
    super(message);
  }

  public InternalErrorException(String message, String code) {
    super(message);
    this.code = code;
  }
}
