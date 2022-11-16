package com.jayqqaa12.jbase.cache.provider.redission;

import com.jayqqaa12.jbase.cache.provider.SerializerCache;
import com.jayqqaa12.jbase.cache.serializer.CacheSerializer;
import com.jayqqaa12.jbase.cache.util.CacheException;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * @author wangshuai, <shuai.wang@leyantech.com>
 * @date 2022-05-27.
 */
public class RedissonCache extends SerializerCache {

  private final String region;
  private final String namespace;
  private final RedissonClient client;


  public RedissonCache(CacheSerializer cacheSerializer, String namespace, String region,
      RedissonClient redisClient) {
    super(cacheSerializer);

    this.region = getRegionName(region);
    this.namespace = namespace;
    this.client = redisClient;
  }

  private String getRegionName(String region) {
    if (namespace != null && !namespace.trim().isEmpty()) {
      region = namespace + ":" + region;
    }
    return region;
  }

  private String _key(String key) {
    return this.region + ":" + key;
  }


  @Override
  public void delete(String key) throws CacheException {
    RBucket<byte[]> rBucket = client.getBucket(_key(key));
    rBucket.delete();
  }

  @Override
  public byte[] getByte(String key) throws CacheException {
    RBucket<byte[]> rBucket = client.getBucket(_key(key));
    return rBucket.get();
  }

  @Override
  public void setByte(String key, byte[] value, int timeToLiveInSeconds) throws CacheException {
    RBucket<byte[]> rBucket = client.getBucket(_key(key));
    if (timeToLiveInSeconds > 0) {
      rBucket.set(value, timeToLiveInSeconds, TimeUnit.SECONDS);
    } else {
      rBucket.set(value);
    }
  }
}
