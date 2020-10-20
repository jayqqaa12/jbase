package com.jayqqaa12.jbase.cache.spring.annotation;



import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 使用前请配置
 *
 * <aop:aspectj-autoproxy></aop:aspectj-autoproxy>
 * <context:component-scan base-package="com.jayqqaa12.j2cache.spring"/>
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD, ElementType.TYPE})
public @interface Cache {

    String region() default "";

    String key();

    int expire() default 0;


}
