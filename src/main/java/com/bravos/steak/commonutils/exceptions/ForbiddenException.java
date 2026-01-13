package com.bravos.steak.commonutils.exceptions;

import lombok.Getter;

public class ForbiddenException extends RuntimeException {

  @Getter
  private String code;

  public ForbiddenException() {
    super();
  }

  public ForbiddenException(String message) {
    super(message);
  }

  public ForbiddenException(String message, String code) {
    super(message);
    this.code = code;
  }
}
