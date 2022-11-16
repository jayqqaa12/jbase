package com.jayqqaa12.jbase.cache.provider;

import com.jayqqaa12.jbase.cache.core.Cache;
import com.jayqqaa12.jbase.cache.core.CacheConfig;
import com.jayqqaa12.jbase.cache.util.CacheException;

import java.util.ArrayList;
import java.util.List;

public class CacheProviderGroup {

  private final static int LEVEL1 = 0;
  private final static int LEVEL2 = 1;

  private List<CacheProvider> cacheProviders = new ArrayList<>();


  public CacheProviderGroup(CacheConfig cacheConfig) {

    if (cacheConfig.getProviderClassList().isEmpty()) {
      throw new IllegalArgumentException("cache providers can't null");
    }

    for (String provider : cacheConfig.getProviderClassList()) {

      CacheProvider cacheProvider = null;
      try {
        cacheProvider = (CacheProvider) Class.forName(provider).newInstance();
      } catch (Exception e) {
        throw new CacheException("nonexistent cache providers class");
      }
      cacheProvider.init(cacheConfig);
      this.cacheProviders.add(cacheProvider);

    }

  }

  public Cache getLevel1Provider(String region) {
    return cacheProviders.get(LEVEL1).buildCache(region);
  }

  public Cache getLevel2Provider(String region) {
    return cacheProviders.get(LEVEL2).buildCache(region);
  }


  public Cache getLevel1Provider(String region, Integer expire) {

    if (expire <= 0) {
      return getLevel1Provider(region);
    }
    return cacheProviders.get(LEVEL1).buildCache(region, expire);
  }

  public Cache getLevel2Provider(String region, Integer expire) {

    if (expire <= 0) {
      return getLevel2Provider(region);
    }
    return cacheProviders.get(LEVEL2).buildCache(region, expire);
  }


  public void stop() {
    for (CacheProvider cacheProvider : cacheProviders) {
      cacheProvider.stop();
    }
  }
}
