package com.bravos.steak.commonutils.exceptions;

import lombok.Getter;

public class BadRequestException extends RuntimeException {

  @Getter
  private String code;

  public BadRequestException() {
    super();
  }

  public BadRequestException(String message) {
    super(message);
  }

  public BadRequestException(String message, String code) {
    super(message);
    this.code = code;
  }
}
