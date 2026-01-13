package com.bravos.steak.commonutils.exceptions;

import lombok.Getter;

public class TooManyRequestException extends RuntimeException {

  @Getter
  private String code;

  public TooManyRequestException() {
    super();
  }

  public TooManyRequestException(String message) {
    super(message);
  }

  public TooManyRequestException(String message, String code) {
    super(message);
    this.code = code;
  }

}
