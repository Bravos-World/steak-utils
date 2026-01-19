package com.bravos.steak.commonutils.shared.helper;

import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;
import tools.jackson.databind.ser.std.ToStringSerializer;

import java.math.BigInteger;

/**
 * Provides a default ObjectMapper instance configured with specific settings<br>
 * Numbers are written as strings to avoid IEE JavaScript number precision issues.<br>
 * Unknown properties are ignored during deserialization.
 */
public class DefaultObjectMapper {

  private static final ObjectMapper objectMapper;

  static {
    SimpleModule simpleModule = new SimpleModule();
    simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
    simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
    simpleModule.addSerializer(BigInteger.class, ToStringSerializer.instance);
    objectMapper = JsonMapper.builder()
        .addModule(simpleModule)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .build();
  }


  /**
   * Get the default ObjectMapper instance.
   *
   * @return the ObjectMapper instance
   */
  public static ObjectMapper get() {
    return objectMapper;
  }

}
