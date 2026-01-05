package com.bravos.steak.commonutils.shared.crypto;

public interface Hmac512Service {

  /**
   * Signs the data with the given secret using HMAC-SHA512 algorithm.
   *
   * @param data   the data to be signed
   * @param secret the secret key used for signing
   * @return the HMAC-SHA512 signature
   */
  String signData(String data, String secret);

  /**
   * Signs the data with the given secret using HMAC-SHA512 algorithm.
   *
   * @param data   the data to be signed
   * @param secret the secret key used for signing
   * @return the HMAC-SHA512 signature as byte array
   */
  byte[] signData(byte[] data, String secret);

  /**
   * Verifies the HMAC-SHA512 signature of the given data using the provided secret.
   *
   * @param data      the original data
   * @param secret    the secret key used for signing
   * @param signature the HMAC-SHA512 signature to be verified
   * @return true if the signature is valid, false otherwise
   */
  boolean verifyData(String data, String secret, String signature);

  /**
   * Verifies the HMAC-SHA512 signature of the given data using the provided secret.
   *
   * @param data      the original data
   * @param secret    the secret key used for signing
   * @param signature the HMAC-SHA512 signature to be verified
   * @return true if the signature is valid, false otherwise
   */
  boolean verifyData(byte[] data, String secret, byte[] signature);

}
