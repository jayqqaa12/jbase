package com.jayqqaa12.jbase.spring.boot;

import com.jayqqaa12.jbase.spring.boot.config.FiegnConfig;
import com.jayqqaa12.jbase.spring.boot.config.MvcConfig;
import com.jayqqaa12.jbase.spring.boot.config.RestConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({
        MvcConfig.class,
        RestConfig.class,
        FiegnConfig.class,
})
@EnableBasic
@EnableDb
public @interface EnableWeb {

}
