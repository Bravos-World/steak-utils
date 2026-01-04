package com.bravos.steak.commonutils.shared.crypto.impl;

import com.bravos.steak.commonutils.shared.crypto.Hmac512Service;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Slf4j
public class Hmac512ServiceImpl implements Hmac512Service {

  @Override
  public String signData(String data, String secret) {
    try {
      byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
      SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "HmacSHA512");
      Mac mac = Mac.getInstance("HmacSHA512");
      mac.init(secretKeySpec);
      byte[] hmacBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
      return Base64.getUrlEncoder().withoutPadding().encodeToString(hmacBytes);
    } catch (NoSuchAlgorithmException | InvalidKeyException e) {
      throw new RuntimeException("Error signing data with HMAC SHA-512", e);
    }
  }

  @Override
  public byte[] signData(byte[] data, String secret) {
    try {
      byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
      SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "HmacSHA512");
      Mac mac = Mac.getInstance("HmacSHA512");
      mac.init(secretKeySpec);
      return mac.doFinal(data);
    } catch (NoSuchAlgorithmException | InvalidKeyException e) {
      throw new RuntimeException("Error signing data with HMAC SHA-512", e);
    }
  }

  @Override
  public boolean verifyData(String data, String secret, String signature) {
    try {
      String signData = signData(data, secret);
      return MessageDigest.isEqual(
          signData.getBytes(StandardCharsets.UTF_8),
          signature.getBytes(StandardCharsets.UTF_8)
      );
    } catch (Exception e) {
      log.error("Error verifying HMAC SHA-512 signature", e);
      return false;
    }
  }

  @Override
  public boolean verifyData(byte[] data, String secret, byte[] signature) {
    try {
      byte[] signData = signData(data, secret);
      return MessageDigest.isEqual(signData, signature);
    } catch (Exception e) {
      log.error("Error verifying HMAC SHA-512 signature", e);
      return false;
    }
  }

}
