package com.jayqqaa12.jbase.spring.mqtt.handler;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface TopicMapping {
    @AliasFor("topic")
    String value() default ""  ;

    @AliasFor("value")
    String topic() default "" ;

    
    


}
