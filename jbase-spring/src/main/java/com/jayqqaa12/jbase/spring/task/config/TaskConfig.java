// Copyright 2022 Leyantech Ltd. All Rights Reserved.

package com.jayqqaa12.jbase.spring.task.config;

import com.jayqqaa12.jbase.spring.exception.BusinessException;
import com.jayqqaa12.jbase.spring.task.TaskManger;
import com.jayqqaa12.jbase.spring.task.config.TaskConfigLoad;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author 12
 */
@Configuration
@Import(TaskManger.class)
public class TaskConfig {

  @Bean
  @ConditionalOnMissingBean
  TaskConfigSource taskConfigSource() {
    return TaskConfigSource.APOLLO;
  }

  @Bean
  @ConditionalOnMissingBean
  public TaskConfigLoad taskConfigLoad(TaskConfigSource source) {
    if (source == TaskConfigSource.APOLLO) {
      return new ApolloTaskConfigLoad();
    }
    return new TaskConfigLoad() {
      @Override
      public String loadCronConfig(String key) {
        throw new BusinessException("config source not implements");
      }
    };
  }


}
