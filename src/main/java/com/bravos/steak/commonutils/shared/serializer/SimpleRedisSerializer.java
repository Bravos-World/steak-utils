package com.bravos.steak.commonutils.shared.serializer;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

/**
 * A simple Redis serializer that handles various data types including: <br>
 * - String <br>
 * - byte[] <br>
 * - Character <br>
 * - Boolean <br>
 * - Enum <br>
 * - Number (Byte, Short, Integer, Long, Float, Double, BigDecimal) <br>
 * - Other Objects (serialized as JSON) <br>
 * Each serialized value is prefixed with a type identifier byte to facilitate proper deserialization. <br>
 * Value serialization format: [type (1 byte)][data...] <br>
 * Type identifiers are defined in {@link RedisValueType}. <br>
 */
@RequiredArgsConstructor
public class SimpleRedisSerializer implements RedisSerializer<Object> {

  private final ObjectMapper objectMapper;

  @Override
  @NullMarked
  public byte[] serialize(@Nullable Object value) throws SerializationException {
    if (value == null) return new byte[0];

    byte type;
    byte[] data;

    switch (value) {
      case String str -> {
        type = RedisValueType.STRING.code;
        data = str.getBytes(StandardCharsets.UTF_8);
      }
      case byte[] bytes -> {
        type = RedisValueType.BYTE_ARRAY.code;
        data = bytes;
      }
      case Character chr -> {
        type = RedisValueType.CHARACTER.code;
        data = chr.toString().getBytes(StandardCharsets.UTF_8);
      }
      case Boolean bool -> {
        type = RedisValueType.BOOLEAN.code;
        data = bool ? new byte[]{1} : new byte[]{0};
      }
      case Enum<?> enm -> {
        type = RedisValueType.ENUM.code;
        data = enm.name().getBytes(StandardCharsets.UTF_8);
      }
      case Number number -> {
        switch (number) {
          case Byte _ -> type = RedisValueType.INT8.code;
          case Short _ -> type = RedisValueType.INT16.code;
          case Integer _ -> type = RedisValueType.INT32.code;
          case Long _ -> type = RedisValueType.INT64.code;
          case Float _ -> type = RedisValueType.FLOAT32.code;
          case Double _ -> type = RedisValueType.FLOAT64.code;
          case BigDecimal _ -> type = RedisValueType.BIG_DECIMAL.code;
          default -> type = RedisValueType.OBJECT.code;
        }
        data = number.toString().getBytes(StandardCharsets.UTF_8);
      }
      default -> {
        type = RedisValueType.OBJECT.code;
        data = objectMapper.writeValueAsBytes(value);
      }
    }

    byte[] result = new byte[data.length + 1];
    result[0] = type;
    System.arraycopy(data, 0, result, 1, data.length);
    return result;
  }

  @Override
  public @Nullable Object deserialize(byte @Nullable [] bytes) throws SerializationException {
    if (bytes == null || bytes.length == 0) return null;
    RedisValueType type = RedisValueType.fromCode(bytes[0]);
    byte[] data = new byte[bytes.length - 1];
    System.arraycopy(bytes, 1, data, 0, data.length);
    try {
      return switch (type) {
        case STRING, ENUM -> new String(data, StandardCharsets.UTF_8);
        case BYTE_ARRAY -> data;
        case CHARACTER -> new String(data, StandardCharsets.UTF_8).charAt(0);
        case BOOLEAN -> data[0] != 0;
        case INT8 -> Byte.parseByte(new String(data, StandardCharsets.UTF_8));
        case INT16 -> Short.parseShort(new String(data, StandardCharsets.UTF_8));
        case INT32 -> Integer.parseInt(new String(data, StandardCharsets.UTF_8));
        case INT64 -> Long.parseLong(new String(data, StandardCharsets.UTF_8));
        case FLOAT32 -> Float.parseFloat(new String(data, StandardCharsets.UTF_8));
        case FLOAT64 -> Double.parseDouble(new String(data, StandardCharsets.UTF_8));
        case BIG_DECIMAL -> new BigDecimal(new String(data, StandardCharsets.UTF_8));
        case OBJECT -> objectMapper.readValue(data, Object.class);
      };
    } catch (Exception e) {
      throw new SerializationException("Failed to deserialize data", e);
    }
  }

}
