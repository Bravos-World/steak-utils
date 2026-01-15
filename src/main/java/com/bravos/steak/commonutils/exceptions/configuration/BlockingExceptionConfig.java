package com.bravos.steak.commonutils.exceptions.configuration;

import com.bravos.steak.commonutils.exceptions.handler.BlockingGlobalException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BlockingExceptionConfig {

  @Bean
  public BlockingGlobalException reactiveExceptionHandler() {
    return new BlockingGlobalException();
  }

}
