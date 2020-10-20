package com.jayqqaa12.jbase.cache.spring.boot.config;

import com.jayqqaa12.jbase.cache.core.CacheConfig;
import com.jayqqaa12.jbase.cache.core.JbaseCache;
import com.jayqqaa12.jbase.cache.spring.SpelKeyGenerator;
import com.jayqqaa12.jbase.cache.spring.aspect.CacheAspect;
import com.jayqqaa12.jbase.cache.spring.aspect.CacheClearArrayAspect;
import com.jayqqaa12.jbase.cache.spring.aspect.CacheClearAspect;
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
    SpelKeyGenerator.class,
    SpringBootRedisConfig.class

})
public class J2CacheAutoConfig {

  @Autowired
  SpringBootRedisConfig springBootRedisConfig;

  @Autowired
  CacheConfig cacheConfig;


  @Bean
  @ConditionalOnMissingBean
  public JbaseCache jbaseCache() {

    cacheConfig.getLettuceConfig().setHosts(springBootRedisConfig.getHost());
    cacheConfig.getLettuceConfig().setDatabase(springBootRedisConfig.getDatabase());
    cacheConfig.getLettuceConfig().setPassword(springBootRedisConfig.getPassword());

    return JbaseCache.build(cacheConfig);
  }


}
