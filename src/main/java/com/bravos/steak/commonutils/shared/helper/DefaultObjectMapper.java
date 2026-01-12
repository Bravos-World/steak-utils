package com.bravos.steak.commonutils.shared.helper;

import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

/**
 * Provides a default ObjectMapper instance configured with specific settings<br>
 * Numbers are written as strings to avoid IEE JavaScript number precision issues.<br>
 * Unknown properties are ignored during deserialization.
 */
public class DefaultObjectMapper {

  private static final ObjectMapper INSTANCE = JsonMapper.builder()
      .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
      .findAndAddModules()
      .build();

  /**
   * Get the default ObjectMapper instance.
   *
   * @return the ObjectMapper instance
   */
  public static ObjectMapper get() {
    return INSTANCE;
  }

}
