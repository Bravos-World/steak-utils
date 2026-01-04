package com.bravos.steak.commonutils.shared.serializer;

public enum RedisValueType {

  STRING((byte) 1),
  BYTE_ARRAY((byte) 2),
  CHARACTER((byte) 3),
  INT8((byte) 4),
  INT16((byte) 5),
  INT32((byte) 6),
  INT64((byte) 7),
  FLOAT32((byte) 8),
  FLOAT64((byte) 9),
  BIG_DECIMAL((byte) 10),
  BOOLEAN((byte) 11),
  ENUM((byte) 12),
  OBJECT((byte) 13);

  public final byte code;

  RedisValueType(byte code) {
    this.code = code;
  }

  public static RedisValueType fromCode(byte code) {
    for (RedisValueType type : values()) {
      if (type.code == code) {
        return type;
      }
    }
    throw new IllegalArgumentException("Unknown type code: " + code);
  }

}
