package com.jayqqaa12.jbase.cache.core;

import static com.jayqqaa12.jbase.cache.core.CacheConst.REFRESH_MIN_TIME;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CacheObject<T> implements Serializable {

  private String region;
  private String key;
  private T value;

  private long loadTime;


  /**
   * 过期时长 单位：秒
   */
  private int expire;

  public boolean canAutoLoad() {

    return  
        (System.currentTimeMillis() - loadTime > expire * 900);
  }


  public boolean isEmpty() {
    return value == null;
  }


}
