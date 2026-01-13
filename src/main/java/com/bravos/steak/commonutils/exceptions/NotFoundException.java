package com.bravos.steak.commonutils.exceptions;

import lombok.Getter;

public class NotFoundException extends RuntimeException {

  @Getter
  private String code;

  public NotFoundException() {
    super();
  }

  public NotFoundException(String message) {
    super(message);
  }

  public NotFoundException(String message, String code) {
    super(message);
    this.code = code;
  }

}
