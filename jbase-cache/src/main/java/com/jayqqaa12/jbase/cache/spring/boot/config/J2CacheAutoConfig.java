package com.jayqqaa12.jbase.cache.spring.boot.config;

import com.jayqqaa12.jbase.cache.core.CacheConfig;
import com.jayqqaa12.jbase.cache.core.JbaseCache;
import com.jayqqaa12.jbase.cache.provider.caffeine.CaffeineCacheProvider;
import com.jayqqaa12.jbase.cache.provider.lettuce.LettuceCacheProvider;
import com.jayqqaa12.jbase.cache.spring.aspect.SpelKeyGenerator;
import com.jayqqaa12.jbase.cache.spring.aspect.CacheAspect;
import com.jayqqaa12.jbase.cache.spring.aspect.CacheClearArrayAspect;
import com.jayqqaa12.jbase.cache.spring.aspect.CacheClearAspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;


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
    cacheConfig.getProviderClassList().add(LettuceCacheProvider.class.getName());
    return cacheConfig;
  }


  @Bean
  @ConditionalOnMissingBean
  public JbaseCache jbaseCache(CacheConfig cacheConfig) {

    cacheConfig.getLettuceConfig()
        .setHosts(springBootRedisConfig.getHost() + ":" + springBootRedisConfig.getPort());
    cacheConfig.getLettuceConfig().setDatabase(springBootRedisConfig.getDatabase());
    cacheConfig.getLettuceConfig().setPassword(springBootRedisConfig.getPassword());

    return JbaseCache.build(cacheConfig);
  }


}
