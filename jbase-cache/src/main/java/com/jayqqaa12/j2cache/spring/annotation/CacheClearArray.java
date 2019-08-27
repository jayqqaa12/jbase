package com.jayqqaa12.j2cache.spring.annotation;


import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD, ElementType.TYPE})
public  @interface CacheClearArray {

    CacheClear[] value();
}
