package com.jayqqaa12.jbase.cache.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class LockKit {

  private static Map<String, ReentrantLock> lockMap = new ConcurrentHashMap<>();


  public static ReentrantLock getLock(String region, String key) {

    String lockKey = key + '@' + region;

    lockMap.computeIfAbsent(lockKey, k -> new ReentrantLock());

    return lockMap.get(lockKey);

  }

  public static void returnLock(String region, String key) {
    String lockKey = key + '@' + region;

    lockMap.remove(lockKey).unlock();
  }


}
