package com.jayqqaa12.jbase.cache.core.load;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class AutoLoadSchedule implements Runnable {

  public static final long MAX_IDLE_TIME = 60 * 1000 * 10;
  private final AutoLoadPool autoLoadPool;
  private ScheduledExecutorService scheduledExecutorService;
  private ExecutorService executorService;


  public AutoLoadSchedule(int threadSize) {
    this.autoLoadPool = new AutoLoadPool();
    scheduledExecutorService = new ScheduledThreadPoolExecutor(1);
    scheduledExecutorService.scheduleAtFixedRate(this, 1, 1, TimeUnit.SECONDS);

    executorService = new ThreadPoolExecutor(threadSize, threadSize, 60, TimeUnit.SECONDS,
        new ArrayBlockingQueue<>(1000));

  }

  public void shutdown() {
    autoLoadPool.clear();
    executorService.shutdown();
    executorService.shutdown();
  }


  @Override
  public void run() {
    for (AutoLoadObject autoLoadObject : autoLoadPool.values()) {
      if (autoLoadObject.canAutoLoad()) {
        executorService.execute(() -> {
          //如果可加载 就进行加载
          try {
            log.info("start auto load data  key {}@{}", autoLoadObject.getKey(),
                autoLoadObject.getRegion());
            autoLoadObject.setLock(true);
            autoLoadObject.getJbaseCache().get(autoLoadObject.getRegion(),
                autoLoadObject.getKey(), autoLoadObject.getFunction(),
                autoLoadObject.getExpire());
            autoLoadObject.setLastUpdateTime(System.currentTimeMillis());

          } catch (Exception e) {
            log.error("auto load data error {}", e);
          } finally {
            autoLoadObject.setLock(false);
          }
        });
      } else {
        // 清理很久没有调用的
        if (autoLoadObject.getLastUpdateTime() > MAX_IDLE_TIME && autoLoadObject.isExpire()) {
          remove(autoLoadObject.getRegion(), autoLoadObject.getKey());

          log.info("clear idle obj  key {}@{}", autoLoadObject.getKey(),
              autoLoadObject.getRegion());
        }
      }
    }
  }


  public void add(AutoLoadObject autoLoadObject) {
    // expire<=0 的表示不过期 不自动加载
    if (autoLoadObject.getExpire() > 0) {
      autoLoadPool.put(autoLoadObject);
    }

  }


  public void remove(String region, String key) {
    autoLoadPool.remove(region, key);
  }

}
