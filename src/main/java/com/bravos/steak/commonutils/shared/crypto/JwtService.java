package com.bravos.steak.commonutils.shared.crypto;


import com.bravos.steak.commonutils.shared.type.JwtTokenClaims;

public interface JwtService {

  /**
   * Generate JWT token from claims
   * @param jwtTokenClaims the claims to be included in the token
   * @return the generated JWT token
   */
  String generateToken(JwtTokenClaims jwtTokenClaims);

  /**
   * Extract claims from JWT token
   * @param token the JWT token
   * @return the extracted claims
   */
  JwtTokenClaims getClaims(String token);

}
