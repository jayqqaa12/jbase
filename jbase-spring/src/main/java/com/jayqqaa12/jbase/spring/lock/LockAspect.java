package com.jayqqaa12.jbase.spring.lock;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.redis.util.RedisLockRegistry;


@Aspect
public class LockAspect {

  @Autowired
  private RedisLockRegistry lockRegistry;


  @Around("@annotation(cache)")
  public Object interceptor(ProceedingJoinPoint invocation, Lock cache) throws Throwable {
    String key = SpelKeyGenerator.buildKey(cache.key(), invocation);
    java.util.concurrent.locks.Lock lock = lockRegistry.obtain(key);
    try {
      lock.lock();
      return invocation.proceed();
    } finally {
      lock.unlock();
    }
  }


}
