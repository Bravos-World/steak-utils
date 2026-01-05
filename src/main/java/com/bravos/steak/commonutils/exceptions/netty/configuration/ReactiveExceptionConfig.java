package com.bravos.steak.commonutils.exceptions.netty.configuration;

import com.bravos.steak.commonutils.exceptions.netty.handler.ReactiveExceptionHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReactiveExceptionConfig {

  @Bean
  public ReactiveExceptionHandler reactiveExceptionHandler(ApplicationContext applicationContext) {
    try {
      Class<?> blockingConfigClass = Class.forName("com.bravos.steak.commonutils.exceptions.tomcat.configuration.BlockingExceptionConfig");
      if (applicationContext.getBeanNamesForType(blockingConfigClass).length > 0) {
        throw new IllegalStateException(
            "Cannot enable both @EnableWebFluxGlobalException and @EnableBlockingGlobalException. " +
                "Please use only one exception handler type (reactive for Netty/WebFlux or blocking for Tomcat)."
        );
      }
    } catch (ClassNotFoundException e) {
      // BlockingExceptionConfig not in classpath, safe to proceed
    }
    return new ReactiveExceptionHandler();
  }

}


