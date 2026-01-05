package com.bravos.steak.commonutils.shared.crypto.impl;

import com.bravos.steak.commonutils.exceptions.UnauthorizeException;
import com.bravos.steak.commonutils.shared.crypto.JwtService;
import com.bravos.steak.commonutils.shared.crypto.RSAService;
import com.bravos.steak.commonutils.shared.helper.DateTimeHelper;
import com.bravos.steak.commonutils.shared.type.GeneralKeyPair;
import com.bravos.steak.commonutils.shared.type.JwtTokenClaims;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

import java.util.Base64;
import java.util.Map;

@Slf4j
public class JwtServiceImpl implements JwtService {

  private final ObjectMapper objectMapper;
  private final RSAService rSAService;

  private final String header;
  private final GeneralKeyPair generalKeyPair;

  public JwtServiceImpl(RSAService rSAService,
                        GeneralKeyPair generalKeyPair
  ) {
    this.objectMapper = new ObjectMapper();
    this.rSAService = rSAService;

    this.header = Base64.getUrlEncoder()
        .withoutPadding()
        .encodeToString(objectMapper.writeValueAsBytes(Map.of(
            "alg", "RS256",
            "typ", "JWT"
        ))) + ".";
    this.generalKeyPair = generalKeyPair;
  }

  @Override
  public String generateToken(JwtTokenClaims jwtTokenClaims) {
    if (generalKeyPair == null || generalKeyPair.getPrivateKey() == null) {
      throw new UnsupportedOperationException("JWT generation is not supported. Missing private key.");
    }
    try {
      StringBuilder token = new StringBuilder(1024);

      String headerPayload = header + Base64.getUrlEncoder()
          .withoutPadding()
          .encodeToString(objectMapper.writeValueAsBytes(jwtTokenClaims.toMap()));

      token.append(headerPayload)
          .append(".")
          .append(rSAService.getSignatureData(headerPayload, generalKeyPair.getPrivateKey()));

      return token.toString();
    } catch (Exception e) {
      log.error("Failed to generate JWT: {}", e.getMessage(), e);
      throw new RuntimeException("Failed to generate JWT: " + e.getMessage());
    }
  }

  @Override
  public JwtTokenClaims getClaims(String token) {
    if (generalKeyPair == null || generalKeyPair.getPublicKey() == null) {
      throw new UnsupportedOperationException("JWT validation is not supported. Missing public key.");
    }
    if (token == null || token.isBlank()) {
      throw new UnauthorizeException("Token is invalid");
    }
    String[] parts = token.split("\\.");
    if (parts.length != 3) {
      throw new UnauthorizeException("Token is invalid");
    }
    if (!rSAService.verifyData(parts[0] + "." + parts[1], parts[2], generalKeyPair.getPublicKey())) {
      throw new UnauthorizeException("Token is invalid");
    }
    JwtTokenClaims payload = objectMapper.readValue(Base64.getUrlDecoder().decode(parts[1]), JwtTokenClaims.class);
    long now = DateTimeHelper.currentTimeMillis();
    if (payload == null || payload.getExp() < now || payload.getIat() > now) {
      throw new UnauthorizeException("Token is expired or not yet valid");
    }
    return payload;
  }

}
