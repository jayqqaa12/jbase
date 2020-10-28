package com.jayqqaa12.jbase.cache.notify;

import com.jayqqaa12.jbase.cache.core.CacheConfig;
import com.jayqqaa12.jbase.cache.core.JbaseCache;

public class NullNotify implements Notify{

  @Override
  public void init(CacheConfig cacheConfig, JbaseCache cache) {
    
  }

  @Override
  public void send(Command command) {
    
  }

  @Override
  public void stop() {

  }

 
}
