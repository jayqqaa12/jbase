package com.jayqqaa12.jbase.web.spring.distributelock.annontation;


import com.jayqqaa12.jbase.web.spring.distributelock.DistributeLock;
import com.jayqqaa12.jbase.web.spring.distributelock.DistributeLockFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 12
 */
@Aspect
@Service
public class ZKLockAspect {

    private static SpelKeyGenerator keyParser = new SpelKeyGenerator();


    @Autowired
    DistributeLockFactory distributeLockFactory;


    @Pointcut("@annotation(org.songbai.variety.utils.base.lock.annotation.ZKLock)")
    public void aspect() {
    }

    @Around("aspect()&&@annotation(lock)")
    public Object interceptor(ProceedingJoinPoint invocation, ZKLock lock)
            throws Throwable {
        DistributeLock distributeLock = null;
        try {
            String key = keyParser.buildKey(lock.key(), invocation).toString();
            distributeLock = distributeLockFactory.newLock(key);
            distributeLock.lock();
            return invocation.proceed();
        } finally {
            if (distributeLock != null) distributeLock.unlock();
        }

    }

}
