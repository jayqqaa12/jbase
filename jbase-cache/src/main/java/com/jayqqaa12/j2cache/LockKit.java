package com.jayqqaa12.j2cache;

import com.jayqqaa12.j2cache.redis.RedisCacheProvider;
import com.jayqqaa12.j2cache.redis.lock.JedisLock;

import java.util.concurrent.TimeUnit;

/**
 * Created by 12 on 2017/7/14.
 * <p>
 */
public class LockKit {

//    public String isLimit(int limit, int timeout) {
//        return JedisRateLimiter.acquireTokenFromBucket(RedisCacheProvider.getClient(), limit, timeout);
//    }

    
    public boolean isLock(String key, int lockExpire) {
        return new JedisLock(RedisCacheProvider.getClient()).tryLock(key, lockExpire);
    }
    public void unlock(String key) {
        new JedisLock(RedisCacheProvider.getClient()).unlock(key);
    }


    public boolean spinLock(String key, int lockExpire) {
        return spinLock(key, lockExpire, 20);
    }

    /**
     * 自旋直到获得锁
     */
    public boolean spinLock(String key, int lockExpire, int waitMillSec) {
        if (waitMillSec < 10) waitMillSec = 10;

        while (!isLock(key, lockExpire)) {
            try {
                TimeUnit.MILLISECONDS.sleep(waitMillSec);
            } catch (InterruptedException e) {
                return false;
            }
        }
        return true;
    }


}
