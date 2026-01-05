package com.bravos.steak.commonutils.autoconfig;

import com.bravos.steak.commonutils.shared.crypto.JwtService;
import com.bravos.steak.commonutils.shared.crypto.RSAService;
import com.bravos.steak.commonutils.shared.crypto.impl.JwtServiceImpl;
import com.bravos.steak.commonutils.shared.type.GeneralKeyPair;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnBean(GeneralKeyPair.class)
public class JwtAutoConfiguration {

  @Bean
  @ConditionalOnBean(RSAService.class)
  @ConditionalOnMissingBean(JwtService.class)
  public JwtService jwtService(RSAService rsaService, GeneralKeyPair generalKeyPair) {
    return new JwtServiceImpl(rsaService, generalKeyPair);
  }

}
