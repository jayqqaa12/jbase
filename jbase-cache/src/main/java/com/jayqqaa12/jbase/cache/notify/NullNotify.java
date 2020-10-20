package com.jayqqaa12.jbase.cache.notify;

import com.jayqqaa12.jbase.cache.core.CacheConfig;
import com.jayqqaa12.jbase.cache.provider.CacheProviderGroup;

public class NullNotify implements Notify{

  @Override
  public void init(CacheConfig cacheConfig, CacheProviderGroup cache) {
    
  }

  @Override
  public void send(Command command) {
    
  }

  @Override
  public void stop() {

  }

 
}
