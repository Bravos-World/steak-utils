package com.bravos.steak.commonutils.autoconfig;

import com.bravos.steak.commonutils.shared.serializer.SimpleRedisSerializer;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.protocol.ProtocolVersion;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import tools.jackson.databind.ObjectMapper;

@Configuration
public class RedisAutoConfiguration {

  @Value("${redis.host:localhost}")
  private String redisHost;

  @Value("${redis.port:6379}")
  private int redisPort;

  @Value("${redis.password:}")
  private String redisPassword;

  @Bean
  public RedisConnectionFactory redisConnectionFactory() {
    ClientOptions options = ClientOptions.builder()
        .protocolVersion(ProtocolVersion.RESP2)
        .pingBeforeActivateConnection(true)
        .build();
    LettuceClientConfiguration clientConfiguration = LettuceClientConfiguration.builder()
        .clientOptions(options)
        .build();
    RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
    redisConfig.setHostName(redisHost);
    redisConfig.setPort(redisPort);
    if (redisPassword != null && !redisPassword.isEmpty()) {
      redisConfig.setPassword(redisPassword);
    }
    return new LettuceConnectionFactory(redisConfig, clientConfiguration);
  }

  @Bean
  @ConditionalOnBean(SimpleRedisSerializer.class)
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory,
                                                           SimpleRedisSerializer simpleRedisSerializer) {
    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redisConnectionFactory);
    redisTemplate.setKeySerializer(StringRedisSerializer.UTF_8);
    redisTemplate.setValueSerializer(simpleRedisSerializer);
    redisTemplate.setHashKeySerializer(StringRedisSerializer.UTF_8);
    redisTemplate.setHashValueSerializer(simpleRedisSerializer);
    redisTemplate.setStringSerializer(StringRedisSerializer.UTF_8);
    redisTemplate.afterPropertiesSet();
    return redisTemplate;
  }

  @Bean
  @ConditionalOnBean(ObjectMapper.class)
  public SimpleRedisSerializer simpleRedisSerializer(ObjectMapper objectMapper) {
    return new SimpleRedisSerializer(objectMapper);
  }

}
