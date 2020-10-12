package com.jayqqaa12.jbase.cache.provider;

import com.jayqqaa12.jbase.cache.core.Cache;
import com.jayqqaa12.jbase.cache.core.CacheConfig;

public interface CacheProvider {

  void init(CacheConfig cacheConfig);

  Cache buildCache(String region,int expire)  ;
  Cache buildCache(String region )  ;


  void stop();

   

}
