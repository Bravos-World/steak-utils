package com.bravos.steak.commonutils.autoconfig;

import com.bravos.steak.commonutils.aspect.KafkaAspect;
import com.bravos.steak.commonutils.shared.helper.DefaultObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import tools.jackson.databind.ObjectMapper;

@AutoConfiguration
public class ObjectMapperConfiguration {

  @Bean
  @ConditionalOnMissingBean(ObjectMapper.class)
  public ObjectMapper objectMapper() {
    return DefaultObjectMapper.get();
  }

  @Bean
  @ConditionalOnBean(ObjectMapper.class)
  public KafkaAspect kafkaAspect(ObjectMapper objectMapper) {
    return new KafkaAspect(objectMapper);
  }

}
