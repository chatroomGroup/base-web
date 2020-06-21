package com.cai.web.core

import java.lang.annotation.Documented
import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(value = ElementType.METHOD)
@interface CacheKey {
    String command() default "ping"

    String[] values() default []
}
