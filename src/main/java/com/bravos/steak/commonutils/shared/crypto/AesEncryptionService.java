package com.bravos.steak.commonutils.shared.crypto;

import javax.crypto.SecretKey;

/**
 * Service interface for AES encryption and decryption operations.
 */
public interface AesEncryptionService {

  /**
   * Encrypts the given plain text using AES encryption with the provided secret key.
   *
   * @param plainText the text to be encrypted
   * @param secretKey the secret key used for encryption
   * @return the encrypted text
   */
  String encrypt(String plainText, String secretKey);

  /**
   * Encrypts the given plain text using AES encryption with the provided SecretKey.
   *
   * @param plainText the text to be encrypted
   * @param secretKey the SecretKey used for encryption
   * @return the encrypted text
   */
  String encrypt(String plainText, SecretKey secretKey);

  /**
   * Decrypts the given encrypted text using AES decryption with the provided secret key.
   *
   * @param encryptedText the text to be decrypted
   * @param secretKey the secret key used for decryption
   * @return the decrypted plain text
   */
  String decrypt(String encryptedText, String secretKey);

  /**
   * Decrypts the given encrypted text using AES decryption with the provided SecretKey.
   *
   * @param encryptedText the text to be decrypted
   * @param secretKey the SecretKey used for decryption
   * @return the decrypted plain text
   */
  String decrypt(String encryptedText, SecretKey secretKey);

  /**
   * Generates a new random secret key for AES encryption.
   *
   * @return the generated secret key as a string
   */
  String generateSecretKey();

  /**
   * Converts a string representation of a secret key into a SecretKey object.
   *
   * @param secretKey the string representation of the secret key
   * @return the SecretKey object
   */
  SecretKey convertSecretKey(String secretKey);

}
