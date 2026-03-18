// Copyright 2022 Leyantech Ltd. All Rights Reserved.

package com.jayqqaa12.jbase.spring.task.config;

import com.jayqqaa12.jbase.spring.task.TaskManger;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author 12
 */
public class ApolloTaskConfigLoad implements TaskConfigLoad {

  @Autowired
  private TaskManger taskManger;

  @Override
  public String loadCronConfig(String key) {
    return null;
  }

}
