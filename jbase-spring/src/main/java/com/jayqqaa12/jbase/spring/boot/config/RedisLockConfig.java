// Copyright 2021 Leyantech Ltd. All Rights Reserved.

package com.jayqqaa12.jbase.spring.boot.config;

import com.jayqqaa12.jbase.spring.lock.LockAspect;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.integration.redis.util.RedisLockRegistry;

/**
 * @author 12
 */
@Configuration
@ConditionalOnClass(RedisLockRegistry.class)
public class RedisLockConfig {

  @Bean
  @ConditionalOnMissingBean
  public RedisLockRegistry redisLockRegistry(RedisConnectionFactory redisConnectionFactory) {
    return new RedisLockRegistry(redisConnectionFactory, "lock");
  }


  @Bean
  @ConditionalOnMissingBean
  public LockAspect lockAspect(){
    return new LockAspect();
  }

}
