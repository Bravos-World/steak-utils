package com.bravos.steak.commonutils.shared.serializer;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;
import tools.jackson.databind.ObjectMapper;

/**
 * A simple Kafka serializer that uses Jackson's ObjectMapper to serialize objects to JSON bytes.
 * If the data is already a byte array, it adds a header to indicate this and returns the byte array directly.
 */
@RequiredArgsConstructor
public class SimpleKafkaSerialize implements Serializer<Object> {

  private final ObjectMapper objectMapper;

  @Override
  public byte[] serialize(String topic, Object data) {
    return serialize(topic, null, data);
  }

  @Override
  public byte[] serialize(String topic, Headers headers, Object data) {
    if(data == null) return null;
    if (data instanceof byte[] bytes) {
      headers.add("x-bytes", new byte[] {1});
      return bytes;
    }
    return objectMapper.writeValueAsBytes(data);
  }

}
