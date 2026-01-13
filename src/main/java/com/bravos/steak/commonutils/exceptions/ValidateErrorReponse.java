package com.bravos.steak.commonutils.exceptions;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ValidateErrorReponse extends ErrorResponse {

  private Map<String, String> errors;

  public ValidateErrorReponse(String detail, String code, Map<String, String> errors) {
    super(detail, code);
    this.errors = errors;
  }

}
