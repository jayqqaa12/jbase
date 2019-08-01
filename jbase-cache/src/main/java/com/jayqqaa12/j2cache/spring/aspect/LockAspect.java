package com.jayqqaa12.j2cache.spring.aspect;


import com.jayqqaa12.j2cache.J2Cache;
import com.jayqqaa12.j2cache.spring.SpelKeyGenerator;
import com.jayqqaa12.j2cache.spring.annotation.Lock;
import com.jayqqaa12.jbase.exception.LockException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;

/**
 * @author 12
 */
@Aspect
@Service
@ConditionalOnBean(J2Cache.class)
public class LockAspect implements Ordered {

    public static final String  LOCK="LOCK:";

    @Autowired
    private SpelKeyGenerator keyParser;

    @Autowired
    private J2Cache j2Cache;

    @Pointcut("@annotation(com.jayqqaa12.j2cache.spring.annotation.Lock)")
    public void aspect() {
    }

    @Around("aspect()&&@annotation(lock)")
    public Object interceptor(ProceedingJoinPoint invocation, Lock lock)
            throws Throwable {
        String  key = null;
        boolean getLock = false;
        try {
            key =LOCK+ keyParser.buildKey(lock.key(), invocation).toString();

            if (lock.spain()) {
                getLock = j2Cache.lock().spinLock(key, lock.lockExpire() );
            } else {
                getLock = j2Cache.lock().isLock(key, lock.lockExpire());
            }
            if (getLock) return invocation.proceed();
        } finally {
            if (getLock&&key!=null) j2Cache.lock().unlock(key);
        }

        throw new LockException("get lock fail!!!  ");
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
