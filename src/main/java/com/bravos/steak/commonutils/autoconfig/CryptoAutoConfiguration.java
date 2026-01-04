package com.bravos.steak.commonutils.autoconfig;

import com.bravos.steak.commonutils.shared.crypto.AesEncryptionService;
import com.bravos.steak.commonutils.shared.crypto.Hmac512Service;
import com.bravos.steak.commonutils.shared.crypto.JwtService;
import com.bravos.steak.commonutils.shared.crypto.RSAService;
import com.bravos.steak.commonutils.shared.crypto.impl.AesEncryptionServiceImpl;
import com.bravos.steak.commonutils.shared.crypto.impl.Hmac512ServiceImpl;
import com.bravos.steak.commonutils.shared.crypto.impl.JwtServiceImpl;
import com.bravos.steak.commonutils.shared.crypto.impl.RSAServiceImpl;
import com.bravos.steak.commonutils.shared.type.GeneralKeyPair;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class CryptoAutoConfiguration {

  @ConditionalOnBean({
      RSAService.class,
      GeneralKeyPair.class
  })
  @ConditionalOnMissingBean(JwtService.class)
  @Bean
  public JwtService jwtService(RSAService rsaService, GeneralKeyPair generalKeyPair) {
    return new JwtServiceImpl(rsaService, generalKeyPair);
  }

  @ConditionalOnMissingBean(RSAService.class)
  @Bean
  public RSAService rsaService() {
    return new RSAServiceImpl();
  }

  @ConditionalOnMissingBean(AesEncryptionService.class)
  @Bean
  public AesEncryptionService aesEncryptionService() {
    return new AesEncryptionServiceImpl();
  }

  @ConditionalOnMissingBean(Hmac512Service.class)
  @Bean
  public Hmac512Service hmac512Service() {
    return new Hmac512ServiceImpl();
  }

}
