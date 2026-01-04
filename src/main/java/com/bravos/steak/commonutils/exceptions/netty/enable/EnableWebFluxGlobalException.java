package com.bravos.steak.commonutils.exceptions.netty.enable;

import com.bravos.steak.commonutils.exceptions.netty.configuration.ReactiveExceptionConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ReactiveExceptionConfig.class)
public @interface EnableWebFluxGlobalException {
}
