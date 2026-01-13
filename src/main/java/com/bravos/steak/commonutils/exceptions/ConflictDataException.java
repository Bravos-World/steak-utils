package com.bravos.steak.commonutils.exceptions;

import lombok.Getter;

public class ConflictDataException extends RuntimeException {

  @Getter
  private String code;

  public ConflictDataException() {
    super();
  }

  public ConflictDataException(String message) {
    super(message);
  }

  public ConflictDataException(String message, String code) {
    super(message);
    this.code = code;
  }
}
