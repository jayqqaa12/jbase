package com.jayqqaa12.jbase.cache.provider.caffeine;

import com.jayqqaa12.jbase.cache.core.Cache;
import com.jayqqaa12.jbase.cache.core.CacheObject;
import com.jayqqaa12.jbase.cache.util.CacheException;

public class CaffeineCache implements Cache {

  private  com.github.benmanes.caffeine.cache.Cache<String, CacheObject> cache;

  public CaffeineCache(com.github.benmanes.caffeine.cache.Cache<String, CacheObject> loadingCache) {
    this.cache=loadingCache;
  }

  @Override
  public CacheObject get(String key) throws CacheException {
    return  cache.getIfPresent(key);
  }

  @Override
  public void set(String key, CacheObject value) throws CacheException {
    cache.put(key,value);
  }

  @Override
  public void set(String key, CacheObject value, int expire) throws CacheException {
    set(key,value);
  }

  @Override
  public void delete(String key) throws CacheException {
    cache.invalidate(key);
  }
}
