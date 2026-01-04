package com.bravos.steak.commonutils.exceptions.tomcat.configuration;

import com.bravos.steak.commonutils.exceptions.tomcat.handler.BlockingGlobalException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BlockingExceptionConfig {

  @Bean
  public BlockingGlobalException reactiveExceptionHandler(ApplicationContext applicationContext) {
    try {
      Class<?> reactiveConfigClass = Class.forName("com.bravos.steak.commonutils.exceptions.netty.configuration.ReactiveExceptionConfig");
      if (applicationContext.getBeanNamesForType(reactiveConfigClass).length > 0) {
        throw new IllegalStateException(
            "Cannot enable both @EnableBlockingGlobalException and @EnableWebFluxGlobalException. " +
            "Please use only one exception handler type (blocking for Tomcat or reactive for Netty/WebFlux)."
        );
      }
    } catch (ClassNotFoundException e) {
      // ReactiveExceptionConfig not in classpath, safe to proceed
    }
    return new BlockingGlobalException();
  }

}
