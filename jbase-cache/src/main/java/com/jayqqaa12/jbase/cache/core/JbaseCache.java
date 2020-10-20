package com.jayqqaa12.jbase.cache.core;


import com.jayqqaa12.jbase.cache.core.load.AutoLoadObject;
import com.jayqqaa12.jbase.cache.core.load.AutoLoadSchedule;
import com.jayqqaa12.jbase.cache.notify.Command;
import com.jayqqaa12.jbase.cache.notify.Notify;
import com.jayqqaa12.jbase.cache.provider.CacheProviderGroup;
import com.jayqqaa12.jbase.cache.util.CacheException;
import com.jayqqaa12.jbase.cache.util.LockKit;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Cache操作入口类
 */

@Slf4j
@Data
public class JbaseCache {

  private CacheProviderGroup provider;

  private Notify notify;

  private AutoLoadSchedule autoLoadSchdule;

  private JbaseCache() {

  }


  public static JbaseCache build(CacheConfig cacheConfig) {
    JbaseCache cache = new JbaseCache();
    cache.provider = new CacheProviderGroup(cacheConfig);
    cache.autoLoadSchdule = new AutoLoadSchedule(cacheConfig.getAutoLoadThreadCount(), cache);

    try {
      cache.notify = (Notify) Class.forName(cacheConfig.getNotifyClass()).newInstance();
      cache.notify.init(cacheConfig, cache.provider);
    } catch (Exception e) {
      throw new CacheException("nonexistent notify class ");
    }

    return cache;
  }


  public void stop() {
    provider.stop();
    notify.stop();
    autoLoadSchdule.shutdown();
  }


  public <T> T get(String region, String key, Supplier<Object> function)
      throws CacheException {

    return get(region, key, function, 0);
  }

  public <T> T get(String region, String key, Supplier<Object> function, int expire)
      throws CacheException {

    T obj = get(region, key);
//    有数据就直接返回
    if (obj != null) {
      return obj;
    }

    try {
      LockKit.getLock(region, key).lock();
      obj = get(region, key);
      //    double check
      if (obj != null) {
        return obj;
      }

      StopWatch stopWatch = new StopWatch();
      stopWatch.start();
      Object value = function.get();
      set(region, key, value, expire);

      stopWatch.stop();
      log.info("get from load data key= {}@{} cost time={}ms ", key, region, stopWatch.getTime());
      //添加auto load
      autoLoadSchdule.add(AutoLoadObject.builder()
          .key(key).region(region).expire(expire).function(function)
          .build());

      return (T) value;
    } finally {
      LockKit.returnLock(region, key);
    }
  }


  public <T> T get(String region, String key) throws CacheException {

    CacheObject<T> cacheObject = provider.getLevel1Provider(region).get(key);

    try {
      // 1级有数据就直接返回
      if (cacheObject != null) {

        log.debug("get data from level 1  key={}@{}", region, key);
        return cacheObject.getValue();
      }

      LockKit.getLock(region, key).lock();
      // double check
      cacheObject = provider.getLevel1Provider(region).get(key);
      // 1级有数据就直接返回
      if (cacheObject != null) {
        log.debug("get data from level 1  key={}@{}", region, key);
        return cacheObject.getValue();
      }

      //从2级获取数据
      cacheObject = provider.getLevel2Provider(region).get(key);
      //不为空写入1级缓存
      if (cacheObject != null) {
        provider.getLevel1Provider(region).set(key, cacheObject);
        log.debug("get data from level 2  key={}@{}", region, key);
        return cacheObject.getValue();
      }
    } finally {
      autoLoadSchdule.refresh(region, key);
      LockKit.returnLock(region, key);
    }
    //如果为空就加载数据

    return null;
  }

  public void set(String region, String key, Object value) throws CacheException {
    set(region, key, value, 0);
  }

  public void set(String region, String key, Object value, int expire) throws CacheException {

    CacheObject cacheObject = CacheObject.builder()
        .key(key).region(region)
        .value(value).build();

    provider.getLevel1Provider(region, expire).set(key, cacheObject, expire);
    provider.getLevel2Provider(region, expire).set(key, cacheObject, expire);

    //notify other
    notify.send(new Command(Command.OPT_EVICT_KEY, region, key));
  }

  public void delete(String region, String key) throws CacheException {

    provider.getLevel1Provider(region).delete(key);
    provider.getLevel2Provider(region).delete(key);

    //notify other
    notify.send(new Command(Command.OPT_EVICT_KEY, region, key));
  }


}






