package com.jayqqaa12.jbase.cache.spring.aspect;


import com.jayqqaa12.jbase.cache.core.JbaseCache;
import com.jayqqaa12.jbase.cache.spring.SpelKeyGenerator;
import com.jayqqaa12.jbase.cache.spring.annotation.CacheClear;
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
public class CacheClearAspect {

    @Autowired
    private JbaseCache j2Cache;


    /**
     * 清除缓存在DB操作前
     * <p>
     * 防止DB操作成功缓存更新失败
     * <p>
     * 如果为了一致性完成操作后再清除一次
     *
     * @param invocation
     * @param cacheClear
     * @throws Throwable
     */
    @Around("@annotation(cacheClear)")
    public Object interceptor(ProceedingJoinPoint invocation, CacheClear cacheClear) throws Throwable {
        String region = StringUtils.isEmpty(cacheClear.region()) ? null : cacheClear.region();
        String  key = SpelKeyGenerator.buildKey(cacheClear.key(), invocation);

        j2Cache.delete(region, key);
        Object result = invocation.proceed();
        j2Cache.delete(region, key);


        if (key.toString().contains("null")) {
            log.info("cache remove but key is null  key={},method={}",key,invocation.toString());
        }

        return result;

    }


}
