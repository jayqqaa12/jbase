package com.jayqqaa12.jbase.spring.boot.config;

import com.jayqqaa12.jbase.spring.serialize.FastJsonRedisJsonSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Created by 12 on 2017/6/1.
 */
@Configuration
@ConditionalOnClass(RedisTemplate.class)
public class RedisConfig {



  @Bean
  @ConditionalOnMissingBean
  public RedisTemplate<String,Object>  redisTemplate(RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<String,Object>  template = new RedisTemplate<>();
    template.setConnectionFactory(redisConnectionFactory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setHashKeySerializer(new StringRedisSerializer());
    template.setDefaultSerializer(new FastJsonRedisJsonSerializer<>());
    template.afterPropertiesSet();
    return template;
  }

}
