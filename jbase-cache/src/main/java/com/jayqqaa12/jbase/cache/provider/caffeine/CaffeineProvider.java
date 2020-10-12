package com.jayqqaa12.jbase.cache.provider.caffeine;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.jayqqaa12.jbase.cache.core.Cache;
import com.jayqqaa12.jbase.cache.core.CacheConfig;
import com.jayqqaa12.jbase.cache.core.CacheConfig.CaffeineConfig;
import com.jayqqaa12.jbase.cache.core.CacheObject;
import com.jayqqaa12.jbase.cache.provider.CacheProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class CaffeineProvider implements CacheProvider {

  private CacheConfig cacheConfig;

  private Map<String, Cache> cacheMap = new HashMap<>();
  private CaffeineConfig defualtConfig = new CaffeineConfig();

  @Override
  public void init(CacheConfig cacheConfig) {

    this.cacheConfig = cacheConfig;

    this.defualtConfig.setSize(10000);
    this.defualtConfig.setExpire(60 * 30);

  }

  @Override
  public Cache buildCache(String region, int expire) {
    CaffeineConfig config = cacheConfig.getCaffeineConfig().getOrDefault(region, defualtConfig);

    return newCache(region, config.getSize(), expire);
  }

  private Cache newCache(String region, int size, int expire) {
    return cacheMap.computeIfAbsent(region, v -> {
      Caffeine<Object, Object> caffeine = Caffeine.newBuilder();

      caffeine = caffeine.maximumSize(size);
      if (expire > 0) {
        caffeine = caffeine.expireAfterWrite(expire, TimeUnit.SECONDS);
      }

      com.github.benmanes.caffeine.cache.Cache<String, CacheObject> loadingCache = caffeine.build();
      return new CaffeineCache(loadingCache);
    });
  }

  @Override
  public Cache buildCache(String region) {
    CaffeineConfig config = cacheConfig.getCaffeineConfig().getOrDefault(region, defualtConfig);
    return newCache(region, config.getSize(), config.getExpire());
  }


  @Override
  public void stop() {
    cacheMap.clear();
  }
}
