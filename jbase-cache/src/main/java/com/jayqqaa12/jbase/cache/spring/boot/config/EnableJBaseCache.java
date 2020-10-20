package com.jayqqaa12.jbase.cache.spring.boot.config;


import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({J2CacheAutoConfig.class})
public @interface EnableJBaseCache {

}
