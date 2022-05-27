package com.jayqqaa12.jbase.cache.core;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class CacheConfig {

  /**
   * 缓存提供者 可选
   * <p>
   * com.jayqqaa12.jbase.cache.provider.NullCacheProvider com.jayqqaa12.jbase.cache.provider.caffeine.CaffeineCacheProvider
   * com.jayqqaa12.jbase.cache.provider.lettuce.LettuceCacheProvider
   * <p>
   * 根据加入顺序确定缓存级别 可自定义CacheProvider 进行扩展
   */
  private List<String> providerClassList = new ArrayList<>();

  private String cacheSerializerClass = "com.jayqqaa12.jbase.cache.serializer.FastJsonCacheSerializer";

  /**
   * 自动加载的线程数
   */
  private int autoLoadThreadCount = 5;

  // 具体的缓存中间件相关配置

  private String redisMode = CacheConst.REDIS_MODE_SINGLE;

  private RedisConfig redisConfig = new RedisConfig();

  private NotifyConfig notifyConfig = new NotifyConfig();

  private Map<String, CaffeineConfig> caffeineConfig = new HashMap<>();

  @Data
  public static class CaffeineConfig {

    private int size = 10_000;
    private int expire = 0;

  }

  @Data
  public static class NotifyConfig {

    private String notifyClass = "com.jayqqaa12.jbase.cache.notify.NullNotify";
    private String notifyTopic = CacheConst.DEFAULT_TOPIC;

    private String host;
    private String groupId = CacheConst.DEFAULT_TOPIC;


  }


  @Data
  public class RedisConfig {

    /**
     * 命名空间 可以用来区分不同 项目
     */
    private String namespace = CacheConst.NAMESPACE;
    /**
     * single 单点 cluster 集群
     */
    private String schema = CacheConst.REDIS_MODE_SINGLE;
    private String hosts = "127.0.0.1:6379";
    private String password = "";
    private int database = 0;
    private int maxTotal = 100;
    private int maxIdle = 10;
    private int minIdle = 10;
    private int timeout = 10000;
    private int clusterTopologyRefresh = 3000;


  }


}
