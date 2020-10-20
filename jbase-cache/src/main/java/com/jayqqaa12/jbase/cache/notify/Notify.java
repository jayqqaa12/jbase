package com.jayqqaa12.jbase.cache.notify;

import com.jayqqaa12.jbase.cache.core.CacheConfig;
import com.jayqqaa12.jbase.cache.provider.CacheProviderGroup;

public interface Notify {

  void init(CacheConfig cacheConfig, CacheProviderGroup cache) throws ClassNotFoundException, Exception;


  /**
   *
   * 广播主动删除的cache 
   *
   */
  void send(Command command);


  void stop();




}
