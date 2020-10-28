package com.jayqqaa12.jbase.cache.core.load;

import static com.jayqqaa12.jbase.cache.core.CacheConst.REFRESH_MIN_TIME;

import com.jayqqaa12.jbase.cache.core.CacheObject;
import com.jayqqaa12.jbase.cache.core.JbaseCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;

import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class AutoLoadSchedule implements Runnable {

  public static final long MAX_IDLE_TIME = 60 * 1000 * 10;
  private final AutoLoadPool autoLoadPool;
  private final JbaseCache cache;
  private ScheduledExecutorService scheduledExecutorService;
  private ExecutorService executorService;


  public AutoLoadSchedule(int threadSize, JbaseCache cache) {
    this.autoLoadPool = new AutoLoadPool();
    this.cache = cache;
    scheduledExecutorService = new ScheduledThreadPoolExecutor(1);
    scheduledExecutorService.scheduleAtFixedRate(this, 1, 1, TimeUnit.SECONDS);

    executorService = new ThreadPoolExecutor(threadSize, threadSize,
        0L, TimeUnit.MILLISECONDS,
        new LinkedBlockingQueue<>(1000), (r) -> new Thread(r, "Thread-Jbase-Cache-Auto-Load"));
  }


  public void shutdown() {
    autoLoadPool.clear();
    executorService.shutdown();
    scheduledExecutorService.shutdown();
  }


  @Override
  public void run() {
    for (AutoLoadObject obj : autoLoadPool.values()) {
      if (obj.canAutoLoad()) {
        executorService.execute(() -> {
          //如果可加载 就进行加载
          try {
            obj.setLock(true);

            // 先重新加载2级缓存的 如果也快到期了再加载数据源

            CacheObject cacheObject = cache.getProvider().getLevel2Provider(obj.getRegion())
                .get(obj.getKey());

            if (cacheObject != null && !cacheObject.canAutoLoad()) {

              cache.getProvider().getLevel1Provider(obj.getRegion(), cacheObject.getExpire())
                  .set(obj.getKey(), cacheObject);

              log.info("auto load data from level 2 key {}@{}", obj.getKey(), obj.getRegion());
              return;
            }

            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            cache.set(obj.getRegion(),
                obj.getKey(), obj.getFunction().get(),
                obj.getExpire());
            stopWatch.stop();
            log.info("auto load data from data source  key {}@{} cost time {}ms", obj.getKey(),
                obj.getRegion(), stopWatch.getTime());

            obj.setLastUpdateTime(System.currentTimeMillis());

          } catch (Exception e) {
            log.error("auto load data error {}", e);
          } finally {
            obj.setLock(false);
          }

        });
      } else {
        // 清理很久没有调用的
        if (System.currentTimeMillis() - obj.getLastUpdateTime() > MAX_IDLE_TIME
            && obj.isExpire()) {
          remove(obj.getRegion(), obj.getKey());
        }
      }
    }
  }


  public void add(AutoLoadObject autoLoadObject) {
    // expire<= REFRESH_MIN_TIME 不自动加载
    if (autoLoadObject.getExpire() >= REFRESH_MIN_TIME) {
      autoLoadPool.put(autoLoadObject);
    }
  }


  public void remove(String region, String key) {
    autoLoadPool.remove(region, key);

    log.info("remove auto load obj  key {}@{}", key, region);
  }

  public void refresh(String region, String key) {
    AutoLoadObject autoLoadObject = autoLoadPool.get(region, key);

    if (autoLoadObject != null) {
      autoLoadObject.setLastRequestTime(System.currentTimeMillis());
    }

  }
}
