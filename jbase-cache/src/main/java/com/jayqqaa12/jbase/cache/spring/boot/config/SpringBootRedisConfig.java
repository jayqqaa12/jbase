package com.jayqqaa12.jbase.cache.spring.boot.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by 12 on 2017/9/21.
 */
@Data
@ConditionalOnProperty(value = "config.j2cache.enable", havingValue = "true")
@ConfigurationProperties(prefix = "spring.redis")
public class SpringBootRedisConfig {


  private Integer port;
  private String host;
  private String password;
  private Integer database;


}
