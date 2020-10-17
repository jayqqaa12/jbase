package com.jayqqaa12.jbase.cache.core;

import static org.junit.Assert.assertEquals;

import com.google.common.collect.Lists;
import com.jayqqaa12.jbase.cache.provider.NullCacheProvider;
import com.jayqqaa12.jbase.cache.provider.lettuce.LettuceCacheProvider;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import redis.embedded.RedisServer;

import java.util.concurrent.TimeUnit;

/**
 * @author 12, {@literal <shuai.wang@leyantech.com>}
 * @date 2020-10-12.
 */
public class LettuceTest {

  private static JbaseCache jbaseCache;

  @BeforeClass
  public static void init() {

    RedisServer.builder()
        .port(6666)
        .setting("bind localhost")
        .build().start();

    CacheConfig cacheConfig = new CacheConfig();

    cacheConfig.setProviderClassList(
        Lists.newArrayList(NullCacheProvider.class.getName(),
            LettuceCacheProvider.class.getName()
        ));

    cacheConfig.getLettuceConfig().setHosts("localhost:6666");

    // 使用内嵌redis

    jbaseCache = JbaseCache.build(cacheConfig);

  }

  @Test
  public void testSimple() throws InterruptedException {

    String value = jbaseCache.get("test", "test");

    assertEquals(value, null);

    jbaseCache.set("test", "test", "value");

    value = jbaseCache.get("test", "test");

    assertEquals(value, "value");


  }

  @Test
  public void testTimeout() throws InterruptedException {
    // test timeout

    jbaseCache.set("test-timeout", "timeout", "timeout", 3);

    String
        value = jbaseCache.get("test-timeout", "timeout");
    assertEquals(value, "timeout");

    TimeUnit.SECONDS.sleep(4);

    value = jbaseCache.get("test-timeout", "timeout");
    assertEquals(value, null);

  }


}