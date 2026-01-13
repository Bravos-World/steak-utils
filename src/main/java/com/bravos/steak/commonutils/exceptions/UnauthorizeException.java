package com.bravos.steak.commonutils.exceptions;

import lombok.Getter;

public class UnauthorizeException extends RuntimeException {

  @Getter
  private String code;

  public UnauthorizeException() {
    super();
  }

  public UnauthorizeException(String message) {
    super(message);
  }

  public UnauthorizeException(String message, String code) {
    super(message);
    this.code = code;
  }
}
