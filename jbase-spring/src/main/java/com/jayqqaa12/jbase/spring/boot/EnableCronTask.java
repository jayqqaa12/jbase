package com.jayqqaa12.jbase.spring.boot;


import com.jayqqaa12.jbase.spring.task.config.TaskConfig;

import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({
    TaskConfig.class
})
@EnableScheduling
public @interface EnableCronTask {

}
