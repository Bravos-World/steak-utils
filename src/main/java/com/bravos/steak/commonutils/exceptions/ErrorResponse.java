package com.bravos.steak.commonutils.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ErrorResponse {

  private String detail;

  private String code;

  public ErrorResponse(String detail) {
    this.detail = detail;
  }

  public ErrorResponse(String detail, String code) {
    this.detail = detail;
    this.code = code;
  }

}
