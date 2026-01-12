package com.bravos.steak.commonutils.aspect;

import com.bravos.steak.commonutils.shared.serializer.ObjectType;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import tools.jackson.databind.ObjectMapper;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Aspect
@RequiredArgsConstructor
public class KafkaAspect {

  private final ObjectMapper objectMapper;

  @Around("@annotation(org.springframework.kafka.annotation.KafkaListener)")
  public Object kafkaAspect(ProceedingJoinPoint pjp) {
    Object[] args = pjp.getArgs();
    MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
    Class<?>[] parameterTypes = methodSignature.getParameterTypes();
    for(int i = 0; i < args.length; i++) {
      switch (args[i]) {
        case ObjectType(byte[] data) -> args[i] = objectMapper.readValue(data, parameterTypes[i]);
        case Iterable<?> iterable -> {
          Type type = methodSignature.getMethod().getGenericParameterTypes()[i];
          Type actualTypeArgument = ((ParameterizedType) type).getActualTypeArguments()[0];
          List<Object> deserializedList = new ArrayList<>();
          for (Object item : iterable) {
            if (item instanceof ObjectType(byte[] bytes)) {
              Object deserializedItem = objectMapper.readValue(bytes, objectMapper.constructType(actualTypeArgument));
              deserializedList.add(deserializedItem);
            } else {
              deserializedList.add(item);
            }
          }
          args[i] = deserializedList;
        }
        default -> {
        }
      }
    }
    try {
      return pjp.proceed(args);
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

}
