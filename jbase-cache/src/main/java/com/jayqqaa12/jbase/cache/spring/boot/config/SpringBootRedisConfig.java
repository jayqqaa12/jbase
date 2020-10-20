package com.jayqqaa12.jbase.cache.spring.boot.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by 12 on 2017/9/21.
 */
@ConditionalOnProperty(value = "config.j2cache.enable", havingValue = "true")
@ConfigurationProperties(prefix = "spring.redis")
public class SpringBootRedisConfig {

  private String host;
  private String password;
  private String database;


  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Integer getDatabase() {
    return Integer.parseInt(database);
  }

  public void setDatabase(String database) {
    this.database = database;
  }
}
