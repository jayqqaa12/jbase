package com.jayqqaa12.jbase.cache.spring.boot.config;

import com.jayqqaa12.jbase.cache.core.CacheConfig;
import com.jayqqaa12.jbase.cache.core.JbaseCache;
import com.jayqqaa12.jbase.cache.provider.caffeine.CaffeineCacheProvider;
import com.jayqqaa12.jbase.cache.provider.redission.RedissonCacheProvider;
import com.jayqqaa12.jbase.cache.spring.aspect.CacheAspect;
import com.jayqqaa12.jbase.cache.spring.aspect.CacheClearArrayAspect;
import com.jayqqaa12.jbase.cache.spring.aspect.CacheClearAspect;
import com.jayqqaa12.jbase.cache.spring.aspect.SpelKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;


/**
 * 使用方式 定义CacheConfig并配置来进行使用
 *
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


  @Bean
  @ConditionalOnMissingBean
  public CacheConfig cacheConfig() {
    CacheConfig cacheConfig = new CacheConfig();
    cacheConfig.getProviderClassList().add(CaffeineCacheProvider.class.getName());
    cacheConfig.getProviderClassList().add(RedissonCacheProvider.class.getName());
    return cacheConfig;
  }


  @Bean
  @ConditionalOnMissingBean
  public JbaseCache jbaseCache(CacheConfig cacheConfig) {

    cacheConfig.getRedisConfig()
        .setHosts(springBootRedisConfig.getHost() + ":" + springBootRedisConfig.getPort());
    cacheConfig.getRedisConfig().setDatabase(springBootRedisConfig.getDatabase());
    cacheConfig.getRedisConfig().setPassword(springBootRedisConfig.getPassword());

    return JbaseCache.build(cacheConfig);
  }


}
