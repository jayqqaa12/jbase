package com.jayqqaa12.j2cache.spring.aspect;


import com.jayqqaa12.j2cache.CacheConstans;
import com.jayqqaa12.j2cache.J2Cache;
import com.jayqqaa12.j2cache.spring.SpelKeyGenerator;
import com.jayqqaa12.j2cache.spring.annotation.Cache;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author 12
 */
@Aspect
@Service
public class CacheAspect {


    @Autowired
    private SpelKeyGenerator keyParser;

    @Autowired
    private J2Cache j2Cache;




    @Around("@annotation(cache)")
    public Object interceptor(ProceedingJoinPoint invocation, Cache cache) throws Throwable {
        Object result = null;
        Object key = null;
        int level = cache.level();
        String region = StringUtils.isEmpty(cache.region()) ? null : cache.region();
        boolean nofity = cache.notifyOther();
        key = keyParser.buildKey(cache.key(), invocation);
        if (level == CacheConstans.LEVEL_ALL) result = j2Cache.get(region, key);
        else result = j2Cache.cache().get(level, region, key);
        if (result == null) {
            result = invocation.proceed();
            if (level == CacheConstans.LEVEL_ALL) j2Cache.cache().set(region, key, result, cache.expire(), nofity);
            else j2Cache.cache().set(level, region, key, result, cache.expire(), nofity);
        }
        return result;
    }


}
