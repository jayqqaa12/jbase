package com.jayqqaa12.jbase.cache.core;

import com.jayqqaa12.jbase.cache.util.CacheException;


public interface Cache {

  CacheObject get(String key) throws CacheException;


  void set(String key, CacheObject value) throws CacheException;


  void set(String key, CacheObject value, int expire) throws CacheException;

  void delete(String key) throws CacheException;


}
