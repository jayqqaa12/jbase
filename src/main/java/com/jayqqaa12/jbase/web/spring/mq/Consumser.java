package com.jayqqaa12.jbase.web.spring.mq;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 设定jms的consumer个数
 * @author Boyce
 * 2016年7月15日 上午10:47:49 
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Consumser {
 int value();
}
