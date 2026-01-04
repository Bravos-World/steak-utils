package com.bravos.steak.commonutils.shared.crypto.impl;

import com.bravos.steak.commonutils.shared.crypto.RSAService;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

@Slf4j
public class RSAServiceImpl implements RSAService {

  private static final String ALGORITHM = "RSA";
  private final ObjectMapper objectMapper;
  private final OAEPParameterSpec OAECPARAMS;

  public RSAServiceImpl() {
    this.objectMapper = new ObjectMapper();
    this.OAECPARAMS = new OAEPParameterSpec(
        "SHA-256",
        "MGF1",
        MGF1ParameterSpec.SHA256,
        PSource.PSpecified.DEFAULT
    );
  }

  @Override
  public String encrypt(String plainText, String publicKey) {
    return encrypt(plainText, convertPublicKey(publicKey));
  }

  @Override
  public String encrypt(String plainText, PublicKey publicKey) {
    return encrypt(plainText.getBytes(), publicKey);
  }

  @Override
  public String encrypt(byte[] data, String publicKey) {
    return encrypt(data, convertPublicKey(publicKey));
  }

  @Override
  public String encrypt(byte[] data, PublicKey publicKey) {
    try {
      Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256andMGF1Padding");

      cipher.init(Cipher.ENCRYPT_MODE, publicKey, OAECPARAMS);
      byte[] encryptedData = cipher.doFinal(data);
      return Base64.getEncoder().encodeToString(encryptedData);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
             | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
      log.error("Error when encrypt data: {}", e.getMessage(), e);
      throw new RuntimeException("Error when encrypt data");
    }
  }

  @Override
  public String decrypt(String cipherData, String privateKey) {
    return decrypt(cipherData, convertPrivateKey(privateKey));
  }

  @Override
  public String decrypt(String cipherData, PrivateKey privateKey) {
    return decrypt(Base64.getDecoder().decode(cipherData), privateKey);
  }

  @Override
  public String decrypt(byte[] cipherData, String privateKey) {
    return decrypt(cipherData, convertPrivateKey(privateKey));
  }

  @Override
  public String decrypt(byte[] cipherData, PrivateKey privateKey) {
    try {
      Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256andMGF1Padding");
      cipher.init(Cipher.DECRYPT_MODE, privateKey, OAECPARAMS);
      byte[] decryptedData = cipher.doFinal(cipherData);
      return new String(decryptedData);
    } catch (Exception e) {
      log.error("Error when decrypt data: {}", e.getMessage(), e);
      throw new RuntimeException("Error when decrypt data");
    }
  }

  @Override
  public String getSignatureData(String data, String privateKey) {
    return getSignatureData(data, convertPrivateKey(privateKey), "SHA256withRSA");
  }

  @Override
  public String getSignatureData(String data, PrivateKey privateKey) {
    return getSignatureData(data, privateKey, "SHA256withRSA");
  }

  @Override
  public String getSignatureData(String data, PrivateKey privateKey, String algorithm) {
    try {
      Signature signature = Signature.getInstance(algorithm);
      signature.initSign(privateKey);
      signature.update(data.getBytes(StandardCharsets.UTF_8));
      byte[] signDataBytes = signature.sign();
      return Base64.getUrlEncoder().withoutPadding().encodeToString(signDataBytes);
    } catch (InvalidKeyException | SignatureException | NoSuchAlgorithmException e) {
      log.error("Error when get signature data: {}", e.getMessage(), e);
      throw new RuntimeException(e.getMessage());
    }
  }

  @Override
  public String getSignatureData(String data, String privateKey, String algorithm) {
    return getSignatureData(data, convertPrivateKey(privateKey), algorithm);
  }

  @Override
  public String getSignatureData(Object data, PrivateKey privateKey, String algorithm) {
    try {
      Signature signature = Signature.getInstance(algorithm);
      signature.initSign(privateKey);
      signature.update(objectMapper.writeValueAsString(data).getBytes(StandardCharsets.UTF_8));
      byte[] signDataBytes = signature.sign();
      return Base64.getUrlEncoder().withoutPadding().encodeToString(signDataBytes);
    } catch (InvalidKeyException | SignatureException | NoSuchAlgorithmException e) {
      log.error("Error when get signature data: {}", e.getMessage(), e);
      throw new RuntimeException(e.getMessage());
    }
  }

  @Override
  public boolean verifyData(String data, String signatureData, String publicKey) {
    return verifyData(data, signatureData, convertPublicKey(publicKey));
  }

  @Override
  public boolean verifyData(String data, String signatureData, PublicKey publicKey) {
    try {
      Signature signature = Signature.getInstance("SHA256withRSA");
      byte[] signedDataBytes = Base64.getUrlDecoder().decode(signatureData);
      signature.initVerify(publicKey);
      signature.update(data.getBytes());
      return signature.verify(signedDataBytes);
    } catch (InvalidKeyException | SignatureException | NoSuchAlgorithmException e) {
      log.warn("Failed when verify data: {}", e.getMessage(), e);
      return false;
    }
  }

  @Override
  public String generatePrivateKey() {
    try {
      KeyPair keyPair = this.generateKeyPair();
      PrivateKey privateKey = keyPair.getPrivate();
      return Base64.getEncoder().encodeToString(privateKey.getEncoded());
    } catch (Exception e) {
      log.warn("Failed when gen private key: {}", e.getMessage(), e);
      throw new RuntimeException(e.getMessage());
    }
  }

  @Override
  public String generatePublicKey(String privateKey) {
    try {
      byte[] privateKeyBytes = Base64.getDecoder().decode(privateKey);
      PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
      KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
      RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
      RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(rsaPrivateKey.getModulus(), BigInteger.valueOf(65537));
      RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);
      return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    } catch (InvalidKeySpecException e) {
      throw new IllegalArgumentException("Invalid private key");
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalArgumentException("Invalid algorithm");
    }
  }

  @Override
  public KeyPair generateKeyPair() {
    try {
      KeyPairGenerator generator = KeyPairGenerator.getInstance(ALGORITHM);
      generator.initialize(2048);
      return generator.generateKeyPair();
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  @Override
  public String getPrivateKeyFromKeyPair(KeyPair keyPair) {
    return Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
  }

  @Override
  public String getPublicKeyFromKeyPair(KeyPair keyPair) {
    return Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
  }


}
