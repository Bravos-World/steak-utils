package com.bravos.steak.commonutils.aspect;

import com.bravos.steak.commonutils.shared.serializer.ObjectType;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import tools.jackson.databind.ObjectMapper;

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
      if(args[i] == null) continue;
      if(args[i] instanceof ObjectType(byte[] data)) {
        args[i] = objectMapper.readValue(data, parameterTypes[i]);
      }
    }
    try {
      return pjp.proceed(args);
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

}
