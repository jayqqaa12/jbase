package com.jayqqaa12.jbase.cache.core.load;

import static com.jayqqaa12.jbase.cache.core.CacheConst.REFRESH_MIN_TIME;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import java.util.function.Supplier;

@Data
@Builder
@Slf4j
public class AutoLoadObject {

  private String region;
  private String key;


  //主动传入的加载方法
  private Supplier<Object> function;

  /**
   * 过期时长 单位：秒
   */
  private int expire;


  private volatile boolean isLock;

  /**
   * 最后更新时间
   */
  private volatile Long lastUpdateTime;

  /**
   * 最后一次请求时间 如果没有请求就不刷新缓存
   */
  private volatile Long lastRequestTime;


  /**
   * 判断是否可用自动加载 没有请求或者过期时间太短的都不行
   * <p>
   * 并且要快到过期时间了(9/10)才刷新
   */
  public boolean canAutoLoad() {

    return !isLock
        && expire > REFRESH_MIN_TIME
        &&
        (System.currentTimeMillis() - lastRequestTime < expire * 1000) &&
        (System.currentTimeMillis() - lastUpdateTime > expire * 900);
  }

  public boolean isExpire() {
    return (System.currentTimeMillis() - lastUpdateTime > expire * 1000);
  }


}
