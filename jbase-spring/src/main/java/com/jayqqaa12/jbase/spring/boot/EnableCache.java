package com.jayqqaa12.jbase.spring.boot;


import com.jayqqaa12.j2cache.spring.boot.config.EnableJ2Cache;
import com.jayqqaa12.jbase.spring.boot.config.RedisConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({RedisConfig.class})
@EnableJ2Cache
public @interface EnableCache {
}
