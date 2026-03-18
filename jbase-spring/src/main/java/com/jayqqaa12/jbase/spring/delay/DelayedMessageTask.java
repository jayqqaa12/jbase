package com.jayqqaa12.jbase.spring.delay;

import cn.hutool.extra.spring.SpringUtil;
import org.redisson.RedissonShutdownException;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Consumer;


/**
 * 基于RDelayedQueue 实现分布式延迟消息 默认使用JDK 序列化
 */
public class DelayedMessageTask<T> {
  private RedissonClient redissonClient;


  public DelayedMessageTask(String queueName, Consumer<DelayMessage<T>> consumer) {
    this(queueName, consumer,
        createExecutor("delay-send-task-" + queueName));
  }


  public DelayedMessageTask(String queueName, Consumer<DelayMessage<T>> consumer,
      Executor executor) {
    this.redissonClient = SpringUtil.getBeansOfType(RedissonClient.class).values()
        .stream().findFirst().orElseThrow(() ->
            new RuntimeException("bean [RedissonClient] not found "));

    init(queueName, createExecutor("delay-poll-task-" + queueName), executor, consumer);
  }

  private static ThreadPoolTaskExecutor createExecutor(String name) {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(1);
    executor.setMaxPoolSize(1);
    executor.setQueueCapacity(0);
    executor.setThreadNamePrefix(name);
    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
    executor.initialize();
    return executor;
  }


  private void init(String name,
      ThreadPoolTaskExecutor pollExecutor, Executor executor,
      Consumer<DelayMessage<T>> consumer) {
    RBlockingQueue<DelayMessage<T>> queue = redissonClient.getBlockingQueue(name);
    pollExecutor.execute(pollDelayMsg(name, queue, executor, consumer));
  }


  private Runnable pollDelayMsg(String name, RBlockingQueue<DelayMessage<T>> queue,
      Executor executor,
      Consumer<DelayMessage<T>> consumer) {
    return () -> {
      try {
        if (redissonClient.isShutdown()) {
          return;
        }
        DelayMessage<T> delayReply  = queue.take();

        executor.execute(() -> {
          RSet<Object> set = redissonClient.getSet("remove-key");
          if (set.contains(delayReply.key)) {
            set.remove(delayReply.key);
            return;
          }

          consumer.accept(delayReply);
        });
      } catch (Exception e) {
        if (!(e instanceof RedissonShutdownException)) {
          // log error
        }
      } finally {
        pollDelayMsg(name, queue, executor, consumer);
      }
    };
  }




}
