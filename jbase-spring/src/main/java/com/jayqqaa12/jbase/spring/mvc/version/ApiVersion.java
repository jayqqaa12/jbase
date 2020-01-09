package com.jayqqaa12.jbase.spring.mvc.version;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiVersion {

    int value() default 1;

    int max() default 999999 ;
    int min() default 1;


    /**
     * ASSIGN 指定版本。 只需要设置 value
     * COMPATIBLY 兼容模式，表示 值min 到 值max 都是兼容的 默认模式
     *
     *
     *
     */
    Model model() default Model.COMPATIBLY;




    enum Model {
        ASSIGN, COMPATIBLY 
    }
    


}