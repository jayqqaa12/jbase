package com.jayqqaa12.j2cache.spring.aspect;


import com.jayqqaa12.j2cache.J2Cache;
import com.jayqqaa12.j2cache.spring.SpelKeyGenerator;
import com.jayqqaa12.j2cache.spring.annotation.CacheClear;
import com.jayqqaa12.j2cache.spring.annotation.CacheClearArray;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 12
 */
@Aspect
@Service
@Slf4j
public class CacheClearArrayAspect {

    @Autowired
    private SpelKeyGenerator keyParser;
    @Autowired
    private J2Cache j2Cache;


    /**
     * 清除缓存在DB操作前
     * <p>
     * 防止DB操作成功缓存更新失败
     * <p>
     * 如果为了一致性完成操作后再清除一次
     *
     * @param invocation
     * @param cacheClears
     * @throws Throwable
     */
    @Around("@annotation(cacheClears)")
    public Object interceptor(ProceedingJoinPoint invocation, CacheClearArray cacheClears) throws Throwable {

        for (CacheClear cacheClear : cacheClears.value()) {
            String region = StringUtils.isEmpty(cacheClear.region()) ? null : cacheClear.region();
            Object key = keyParser.buildKey(cacheClear.key(), invocation);
            j2Cache.remove(region, key);

            if (key.toString().contains("null")) {
                log.info("cache remove but key is null  key={},method={}", key, invocation.toString());
            }
        }
        
        Object rest = invocation.proceed();
        for (CacheClear cacheClear : cacheClears.value()) {
            String region = StringUtils.isEmpty(cacheClear.region()) ? null : cacheClear.region();
            Object key = keyParser.buildKey(cacheClear.key(), invocation);
            j2Cache.remove(region, key);

            if (key.toString().contains("null")) {
                log.info("cache remove but key is null  key={},method={}", key, invocation.toString());
            }
        }

        return rest;

    }


}
