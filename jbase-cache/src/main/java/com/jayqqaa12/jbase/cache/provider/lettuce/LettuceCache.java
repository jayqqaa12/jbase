package com.jayqqaa12.jbase.cache.provider.lettuce;

import com.jayqqaa12.jbase.cache.provider.SerializerCache;
import com.jayqqaa12.jbase.cache.serializer.CacheSerializer;
import com.jayqqaa12.jbase.cache.util.CacheException;
import io.lettuce.core.api.StatefulConnection;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.BaseRedisCommands;
import io.lettuce.core.api.sync.RedisKeyCommands;
import io.lettuce.core.api.sync.RedisStringCommands;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import org.apache.commons.pool2.impl.GenericObjectPool;


public class LettuceCache extends SerializerCache {

  private final GenericObjectPool<StatefulConnection<String, byte[]>> pool;
  private final String region;
  private final String namespace;

  public LettuceCache(CacheSerializer cacheSerializer, String namespace, String region,
      GenericObjectPool<StatefulConnection<String, byte[]>> pool) {
    super(cacheSerializer);

    this.pool = pool;
    this.region = getRegionName(region);
    this.namespace = namespace;

  }

  private String getRegionName(String region) {
    if (namespace != null && !namespace.trim().isEmpty()) {
      region = namespace + ":" + region;
    }
    return region;
  }

  private StatefulConnection connect() {
    try {
      return pool.borrowObject();
    } catch (Exception e) {
      throw new CacheException(e);
    }
  }

  private BaseRedisCommands sync(StatefulConnection conn) {
    if (conn instanceof StatefulRedisClusterConnection) {
      return ((StatefulRedisClusterConnection) conn).sync();
    } else if (conn instanceof StatefulRedisConnection) {
      return ((StatefulRedisConnection) conn).sync();
    }
    return null;
  }

  private String _key(String key) {
    return this.region + ":" + key;
  }


  @Override
  public byte[] getByte(String key) {
    try (StatefulConnection<String, byte[]> connection = connect()) {
      RedisStringCommands<String, byte[]> cmd = (RedisStringCommands) sync(connection);
      return cmd.get(_key(key));
    }
  }

  @Override
  public void setByte(String key, byte[] value, int timeToLiveInSeconds) throws CacheException {

    try (StatefulConnection<String, byte[]> connection = this.connect()) {
      RedisStringCommands<String, byte[]> cmd = (RedisStringCommands) this.sync(connection);

      if (timeToLiveInSeconds > 0) {
        cmd.setex(_key(key), timeToLiveInSeconds, value);
      } else {
        cmd.set(_key(key), value);
      }
    }
  }


  @Override
  public void delete(String key) throws CacheException {
    try (StatefulConnection<String, byte[]> connection = this.connect()) {
      RedisKeyCommands<String, byte[]> cmd = (RedisKeyCommands) this.sync(connection);
      cmd.del(_key(key));
    }
  }
}
