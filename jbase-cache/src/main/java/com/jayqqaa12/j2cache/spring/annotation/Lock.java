package com.jayqqaa12.j2cache.spring.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by 12 on 2017/7/14.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD, ElementType.TYPE})
public @interface Lock {

    String key();
    int lockExpire();

    /**
     * 是否自旋等待直到成功
     * @return
     */
    boolean spain() default true ;

    /**
     * 每次自旋等待时间
     * @return
     */
    int spainWaitMillSec() default 20;

}
