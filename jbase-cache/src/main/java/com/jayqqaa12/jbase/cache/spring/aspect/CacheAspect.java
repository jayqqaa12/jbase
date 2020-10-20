package com.jayqqaa12.jbase.cache.spring.aspect;


import com.jayqqaa12.jbase.cache.core.JbaseCache;
import com.jayqqaa12.jbase.cache.spring.SpelKeyGenerator;
import com.jayqqaa12.jbase.cache.spring.annotation.Cache;
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
  private JbaseCache j2Cache;


  @Around("@annotation(cache)")
  public Object interceptor(ProceedingJoinPoint invocation, Cache cache) throws Throwable {
    Object result = null;
    String region = StringUtils.isEmpty(cache.region()) ? null : cache.region();
    String key = SpelKeyGenerator.buildKey(cache.key(), invocation);
    result = j2Cache.get(region, key);
    if (result == null) {
      result = invocation.proceed();
      j2Cache.set(region, key, result, cache.expire());
    }
    return result;
  }


}
