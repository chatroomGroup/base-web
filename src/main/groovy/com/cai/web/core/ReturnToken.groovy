package com.cai.web.core

import java.lang.annotation.*

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(value = [ElementType.TYPE, ElementType.METHOD])
@interface ReturnToken {

}
