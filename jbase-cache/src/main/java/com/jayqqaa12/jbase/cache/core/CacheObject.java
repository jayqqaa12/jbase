package com.jayqqaa12.jbase.cache.core;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class CacheObject<T> implements Serializable {

  private String region;
  private String key;
  private T value;

  /**
   * 过期时长 单位：秒
   */
  private int expire;
 

  public boolean isEmpty() {
    return value == null;
  }


}
