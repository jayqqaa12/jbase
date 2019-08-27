package com.jayqqaa12.j2cache.spring.boot.config;

import com.jayqqaa12.j2cache.J2Cache;
import com.jayqqaa12.j2cache.spring.SpelKeyGenerator;
import com.jayqqaa12.j2cache.spring.aspect.CacheAspect;
import com.jayqqaa12.j2cache.spring.aspect.CacheClearArrayAspect;
import com.jayqqaa12.j2cache.spring.aspect.CacheClearAspect;
import com.jayqqaa12.j2cache.spring.aspect.LockAspect;
import com.jayqqaa12.j2cache.util.ConfigKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;

import java.util.Properties;

/**
 * Created by 12 on 2017/9/20.
 */
@Configuration
@EnableConfigurationProperties({SpringBootRedisConfig.class})
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
@Import({
        CacheAspect.class,
        CacheClearAspect.class,
        CacheClearArrayAspect.class,
        LockAspect.class,
        SpelKeyGenerator.class,
        SpringBootRedisConfig.class

})
public class J2CacheAutoConfig {

    @Autowired
    SpringBootRedisConfig springBootRedisConfig;

    @Bean
    @ConditionalOnMissingBean
    public J2Cache j2Cache() {

        Properties properties = new Properties();

        properties.setProperty("redis.hosts",springBootRedisConfig.getHost());
        properties.setProperty("redis.password",springBootRedisConfig.getPassword());
        properties.setProperty("redis.database",springBootRedisConfig.getDatabase().toString());

        ConfigKit.setProps(properties);

        return new J2Cache();
    }



}
