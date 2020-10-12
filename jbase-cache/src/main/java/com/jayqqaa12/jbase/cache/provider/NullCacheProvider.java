package com.jayqqaa12.jbase.cache.provider;

import com.jayqqaa12.jbase.cache.core.Cache;
import com.jayqqaa12.jbase.cache.core.CacheConfig;
import com.jayqqaa12.jbase.cache.provider.CacheProvider;

public class NullCacheProvider implements CacheProvider {

  public static final NullCache cache=new NullCache();

  @Override
  public void init(CacheConfig cacheConfig) {
  }
  @Override
  public Cache buildCache(String region, int expire) {
    return buildCache(region);
  }

  @Override
  public Cache buildCache(String region) {
    return cache;
  }

  @Override
  public void stop() {

  }
}
