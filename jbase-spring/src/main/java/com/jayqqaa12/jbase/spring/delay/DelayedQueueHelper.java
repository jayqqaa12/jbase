// Copyright 2021 Leyantech Ltd. All Rights Reserved.

package com.jayqqaa12.jbase.spring.delay;

import cn.hutool.extra.spring.SpringUtil;
import com.google.common.collect.Maps;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @author 12
 */
public class DelayedQueueHelper<T> {

  private RedissonClient redissonClient = SpringUtil.getBeansOfType(RedissonClient.class).values()
      .stream().findFirst().orElseThrow(() ->
          new RuntimeException("bean [RedissonClient] not found "));

  private String queueName;

  public DelayedQueueHelper(String queueName) {
    this.queueName = queueName;
  }

  public RDelayedQueue<DelayMessage<T>> getDelayedQueue() {
    RBlockingQueue<DelayMessage<T>> blockingFairQueue = redissonClient
        .getBlockingQueue(queueName);
    return redissonClient.getDelayedQueue(blockingFairQueue);

  }

  public void addQueue(DelayMessage<T> t, long delay, TimeUnit timeUnit) {

    addQueue(t, delay, timeUnit, false);
  }

  public void addQueue(DelayMessage<T> t, long delay, TimeUnit timeUnit, boolean checkContains) {

    if (t == null || delay < 0) {
      return;
    }
    t.setTraceMap(Maps.newHashMap());

    if (!checkContains) {
      addQueueMessage(t, delay, timeUnit);
    } else {
      addQueueMessageAndNotContains(t, delay, timeUnit);
    }
  }


  public void removeQueue(String key) {
    RSet set = redissonClient.getSet("remove-key");
    set.expire(Duration.ofDays(1));
    set.add(key);
  }

  private void addQueueMessage(DelayMessage<T> t, long delay, TimeUnit timeUnit) {
    String key = t.getKey();
    t.setEnqueueAt(System.currentTimeMillis());
    getDelayedQueue().offer(t, delay, timeUnit);
  }


  private void addQueueMessageAndNotContains(DelayMessage<T> t, long delay, TimeUnit timeUnit) {
    String key = t.getKey();
    t.setEnqueueAt(System.currentTimeMillis());
    if (!getDelayedQueue().contains(t)) {
      getDelayedQueue().offer(t, delay, timeUnit);
    }
  }


}
