package com.jayqqaa12.jbase.spring.boot;

import com.jayqqaa12.jbase.spring.boot.config.MvcConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({
        MvcConfig.class,
})
@EnableBasic
@EnableDb
public @interface EnableWeb {

}
