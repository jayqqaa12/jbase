package com.jayqqaa12.jbase.cache.provider.redission;

import static com.jayqqaa12.jbase.cache.core.CacheConst.REDIS_MODE_CLUSTER;

import com.jayqqaa12.jbase.cache.core.Cache;
import com.jayqqaa12.jbase.cache.core.CacheConfig;
import com.jayqqaa12.jbase.cache.provider.CacheProvider;
import com.jayqqaa12.jbase.cache.serializer.CacheSerializer;
import com.jayqqaa12.jbase.cache.util.CacheException;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangshuai, <shuai.wang@leyantech.com>
 * @date 2022-05-27.
 */
public class RedissonCacheProvider implements CacheProvider {


  private final ConcurrentHashMap<String, Cache> regions = new ConcurrentHashMap<>();
  private CacheConfig cacheConfig;
  private RedissonClient redisClient;

  @Override
  public void init(CacheConfig cacheConfig) {
    this.cacheConfig = cacheConfig;

    String hosts = cacheConfig.getRedisConfig().getHosts();
    int database = cacheConfig.getRedisConfig().getDatabase();
    String password = cacheConfig.getRedisConfig().getPassword();

    Config config = new Config();

    if (REDIS_MODE_CLUSTER.equalsIgnoreCase(cacheConfig.getRedisConfig().getSchema())) {
      String[] hostArray = hosts.split(",");
      ClusterServersConfig baseConfig = config
          .useClusterServers()
          .setTimeout(cacheConfig.getRedisConfig().getTimeout())
          .setScanInterval(cacheConfig.getRedisConfig().getClusterTopologyRefresh())
          .setPassword(password)
          ;
      for (String addr : hostArray) {
        baseConfig.addNodeAddress("redis://" + addr);
      }

    } else {
      SingleServerConfig singleServerConfig= config.useSingleServer()
          .setTimeout(cacheConfig.getRedisConfig().getTimeout())
          .setDatabase(database)
          .setAddress("redis://" + hosts);
      if(!StringUtils.isEmpty(password)){
        singleServerConfig.setPassword(password);
      }

    }
    redisClient = Redisson.create(config);

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

      return regions.computeIfAbsent(namespace + ":" + region,
          k -> new RedissonCache(cacheSerializer, namespace, region, redisClient));
    } catch (Exception e) {
      throw new CacheException(e);
    }

  }

  @Override
  public void stop() {
    regions.clear();
    redisClient.shutdown();

  }
}
