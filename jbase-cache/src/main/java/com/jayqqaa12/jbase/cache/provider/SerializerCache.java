package com.jayqqaa12.jbase.cache.provider;

import com.jayqqaa12.jbase.cache.core.Cache;
import com.jayqqaa12.jbase.cache.core.CacheObject;
import com.jayqqaa12.jbase.cache.serializer.CacheSerializer;
import com.jayqqaa12.jbase.cache.util.CacheException;

import java.io.IOException;

public abstract class SerializerCache implements Cache {

  private final CacheSerializer cacheSerializer;

  public SerializerCache(CacheSerializer cacheSerializer) {
    this.cacheSerializer = cacheSerializer;

  }

  @Override
  public CacheObject get(String key) throws CacheException {
    try {
      byte[] bytes = getByte(key);
      return cacheSerializer.deserialize(bytes);
    } catch (IOException e) {
      throw new CacheException(key + " deserialize error ", e);
    }
  }



  @Override
  public void set(String key, CacheObject value) throws CacheException {
    try {
      setByte(key, cacheSerializer.serialize(value),0);
    } catch (IOException e) {
      throw new CacheException(key + " serialize error ", e);
    }

  }


  @Override
  public void set(String key, CacheObject value, int expire) throws CacheException {

    try {
      setByte(key, cacheSerializer.serialize(value), expire);
    } catch (IOException e) {
      throw new CacheException(key + " serialize error ", e);
    }
  }


  public abstract byte[] getByte(String key) throws CacheException;

  public abstract void setByte(String key, byte[] value, int seconds) throws CacheException ;


}
