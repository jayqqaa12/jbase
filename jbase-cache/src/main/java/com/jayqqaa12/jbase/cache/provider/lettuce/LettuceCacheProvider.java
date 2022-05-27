package com.jayqqaa12.jbase.cache.provider.lettuce;

import static com.jayqqaa12.jbase.cache.core.CacheConst.REDIS_MODE_CLUSTER;

import com.jayqqaa12.jbase.cache.core.Cache;
import com.jayqqaa12.jbase.cache.core.CacheConfig;
import com.jayqqaa12.jbase.cache.provider.CacheProvider;
import com.jayqqaa12.jbase.cache.serializer.CacheSerializer;
import com.jayqqaa12.jbase.cache.util.CacheException;
import io.lettuce.core.AbstractRedisClient;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulConnection;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.support.ConnectionPoolSupport;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author jayqqaa12
 */
@Slf4j
public class LettuceCacheProvider implements CacheProvider {

  private static AbstractRedisClient redisClient;
  private GenericObjectPool<StatefulConnection<String, byte[]>> pool;

  private LettuceByteCodec codec = new LettuceByteCodec();

  private final ConcurrentHashMap<String, Cache> regions = new ConcurrentHashMap<>();
  private CacheConfig cacheConfig;

  @Override
  public void init(CacheConfig cacheConfig) {
    this.cacheConfig = cacheConfig;

    String hosts = cacheConfig.getRedisConfig().getHosts();

    int database = cacheConfig.getRedisConfig().getDatabase();
    String password = cacheConfig.getRedisConfig().getPassword();

    int clusterTopologyRefresh = cacheConfig.getRedisConfig().getClusterTopologyRefresh();

    if (REDIS_MODE_CLUSTER.equalsIgnoreCase(cacheConfig.getRedisConfig().getSchema())) {
      List<RedisURI> redisURIs = new ArrayList<>();
      String[] hostArray = hosts.split(",");
      for (String host : hostArray) {
        String[] redisArray = host.split(":");
        RedisURI uri = RedisURI.create(redisArray[0], Integer.valueOf(redisArray[1]));
        uri.setDatabase(database);
        uri.setPassword(password);
//        uri.setSentinelMasterId(sentinelMasterId);
        redisURIs.add(uri);
      }
      redisClient = RedisClusterClient.create(redisURIs);
      ClusterTopologyRefreshOptions topologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
          //开启自适应刷新
          .enableAdaptiveRefreshTrigger(ClusterTopologyRefreshOptions.RefreshTrigger.MOVED_REDIRECT,
              ClusterTopologyRefreshOptions.RefreshTrigger.PERSISTENT_RECONNECTS)
          .enableAllAdaptiveRefreshTriggers()
          .adaptiveRefreshTriggersTimeout(Duration.ofMillis(clusterTopologyRefresh))
          //开启定时刷新,时间间隔根据实际情况修改
          .enablePeriodicRefresh(Duration.ofMillis(clusterTopologyRefresh))
          .build();
      ((RedisClusterClient) redisClient).setOptions(
          ClusterClientOptions.builder().topologyRefreshOptions(topologyRefreshOptions).build());
    } else {
      String[] redisArray = hosts.split(":");
      RedisURI uri = RedisURI.create(redisArray[0], Integer.valueOf(redisArray[1]));
      uri.setDatabase(database);
      uri.setPassword(password);
      redisClient = RedisClient.create(uri);
    }

    try {
      redisClient.setDefaultTimeout(Duration.ofMillis(cacheConfig.getRedisConfig().getTimeout()));
    } catch (Exception e) {
      log.warn("Failed to set default timeout, using default 10000 milliseconds.", e);
    }

    //connection pool configurations
    GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
    poolConfig.setMaxTotal(cacheConfig.getRedisConfig().getMaxTotal());
    poolConfig.setMaxIdle(cacheConfig.getRedisConfig().getMaxIdle());
    poolConfig.setMinIdle(cacheConfig.getRedisConfig().getMinIdle());

    pool = ConnectionPoolSupport.createGenericObjectPool(() -> {
      if (redisClient instanceof RedisClient) {
        return ((RedisClient) redisClient).connect(codec);
      } else if (redisClient instanceof RedisClusterClient) {
        return ((RedisClusterClient) redisClient).connect(codec);
      }
      return null;
    }, poolConfig);

  }

  @Override
  public Cache buildCache(String region, int expire) throws CacheException {
    return buildCache(region);
  }

  @Override
  public Cache buildCache(String region) throws CacheException {

    try {

    CacheSerializer cacheSerializer = (CacheSerializer) Class
        .forName(cacheConfig.getCacheSerializerClass()).newInstance();
    String namespace = this.cacheConfig.getRedisConfig().getNamespace();

    return regions
        .computeIfAbsent(namespace + ":" + region,
            k -> new LettuceCache(cacheSerializer, namespace, region, pool));
    }catch (Exception e){
      throw  new CacheException(e);
    }

  }

  @Override
  public void stop() {

    pool.close();
    regions.clear();
    redisClient.shutdown();

  }
}
