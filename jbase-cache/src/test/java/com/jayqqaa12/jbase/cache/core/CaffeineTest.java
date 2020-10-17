package com.jayqqaa12.jbase.cache.core;

import static org.junit.Assert.*;

import com.google.common.collect.Lists;
import com.jayqqaa12.jbase.cache.provider.NullCacheProvider;
import com.jayqqaa12.jbase.cache.provider.caffeine.CaffeineCacheProvider;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import redis.embedded.RedisServer;

import java.util.concurrent.TimeUnit;

/**
 * @author 12, {@literal <shuai.wang@leyantech.com>}
 * @date 2020-10-12.
 */
@Slf4j
public class CaffeineTest {

  private JbaseCache jbaseCache;

  @Before
  public void init() {

    CacheConfig cacheConfig = new CacheConfig();

    cacheConfig.setProviderClassList(
        Lists.newArrayList(NullCacheProvider.class.getName(),
            CaffeineCacheProvider.class.getName()
        ));

    this.jbaseCache = JbaseCache.build(cacheConfig);

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

    String value = jbaseCache.get("test-timeout", "timeout");
    assertEquals(value, "timeout");

    TimeUnit.SECONDS.sleep(4);

    value = jbaseCache.get("test-timeout", "timeout");
    assertEquals(value, null);

  }

  @Test
  public void testAutoLoad() throws InterruptedException {

    String value = jbaseCache.get("auto-load", "time", () -> "value", 5);

    for (int i = 0; i < 100; i++) {
      //大于120 才会触发auto load
      value = jbaseCache.get("auto-load", "time");

      assertEquals(value, "value");

      TimeUnit.SECONDS.sleep(2);
    }

  }

}