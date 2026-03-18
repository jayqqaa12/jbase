// Copyright 2021 Leyantech Ltd. All Rights Reserved.

package com.jayqqaa12.jbase.spring.task;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 12
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TimerTask {

  //配置apollo key
  String value();

  /**
   * 是否允许多节点执行
   * 默认false会加锁防止同时执行
   * @return
   */
  boolean concurrent() default false;



}
