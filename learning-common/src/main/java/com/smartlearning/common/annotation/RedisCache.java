package com.smartlearning.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Redis缓存注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisCache {

    /**
     * 缓存key，支持SpEL表达式
     */
    String key();

    /**
     * 缓存过期时间，单位：秒，默认3600秒（1小时）
     */
    long timeout() default 3600;

    /**
     * 缓存前缀
     */
    String prefix() default "";
}