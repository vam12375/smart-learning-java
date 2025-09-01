package com.smartlearning.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Redis缓存清除注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisEvict {

    /**
     * 缓存key，支持SpEL表达式
     */
    String key();

    /**
     * 缓存前缀
     */
    String prefix() default "";

    /**
     * 是否清除所有以prefix开头的缓存
     */
    boolean allEntries() default false;
}