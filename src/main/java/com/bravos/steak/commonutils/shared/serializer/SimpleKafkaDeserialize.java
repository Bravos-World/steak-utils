package com.bravos.steak.commonutils.shared.serializer;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;
import tools.jackson.databind.ObjectMapper;

/**
 * A simple Kafka deserializer that uses Jackson's ObjectMapper to deserialize JSON bytes to objects.
 * If a specific header is present, it treats the data as a raw byte array and returns it directly.
 */
@RequiredArgsConstructor
public class SimpleKafkaDeserialize implements Deserializer<Object> {

  private final ObjectMapper objectMapper;

  @Override
  public Object deserialize(String topic, byte[] data) {
    return objectMapper.readValue(data, Object.class);
  }

  @Override
  public Object deserialize(String topic, Headers headers, byte[] data) {
    if (headers.headers("x-bytes").iterator().hasNext()) {
      return data;
    }
    return objectMapper.readValue(data, Object.class);
  }

}
