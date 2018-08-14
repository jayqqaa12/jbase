package com.jayqqaa12.j2cache.spring.boot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by 12 on 2017/9/21.
 */
@ConfigurationProperties(prefix = "spring.redis")
public class SpringBootRedisConfig {

    private String host;
    private String password;
    private String  database;


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

    public void setDatabase(String  database) {
        this.database = database;
    }
}
