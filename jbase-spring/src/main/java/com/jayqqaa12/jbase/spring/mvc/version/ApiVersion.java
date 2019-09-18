package com.jayqqaa12.jbase.spring.mvc.version;


import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

/**
 * 所有的接口默认是使用  兼容模式  在进行处理。
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface ApiVersion {

    /**
     * ASSIGN 指定版本。
     * COMPATIBLY 兼容模式， 表示小于的版本都是兼容的， 会检查是否存在不兼容的版本。 默认模式
     * INCOMPATIBLE 不兼容模式， 表示小于的版本都是不兼容的， 会检查是否存在兼容的版本。
     */
    Model model() default Model.COMPATIBLY;

    /**
     * 当前的版本号
     */
    int value() default 1;

    /**
     * 兼容的版本
     * 向下兼容 1..10 表示兼容版本1到10 ，
     * 部分兼容 1,3,5,6 表示兼容指定版本
     */
    String compatibly() default "";


    /**
     * 不兼容的版本
     * 向下兼容 1..10 表示兼容版本1到10 ，
     * 部分兼容 1,3,5,6 表示兼容指定版本
     */
    String incompatible() default "";


    static enum Model {
        ASSIGN, COMPATIBLY, INCOMPATIBLE
    }
}
