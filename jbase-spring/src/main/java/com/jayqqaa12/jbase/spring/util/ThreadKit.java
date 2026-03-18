// Copyright 2021 Leyantech Ltd. All Rights Reserved.

package com.jayqqaa12.jbase.spring.util;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Optional;

/**
 * @author 12
 */
public class ThreadKit {

  private static final ThreadLocal<Map<String, Object>> LOCAL = new ThreadLocal<>();

  private static final Map<String, Object> EMPTY_MAP = Maps.newHashMapWithExpectedSize(1);

  public static Object get(String key) {
    return Optional.ofNullable(LOCAL.get()).orElse(EMPTY_MAP).get(key);
  }

  public static void set(String key, Object value) {
    Map<String, Object> map = Optional.ofNullable(LOCAL.get()).orElse(Maps.newHashMap());
    map.put(key, value);
    LOCAL.set(map);
  }

  public static void setAll(Map<String, Object> map) {
    LOCAL.set(map);
  }

  public static Map<String, Object> getAll() {
    return LOCAL.get();
  }


  public static void clear(String key) {
    Optional.ofNullable(LOCAL.get()).orElse(EMPTY_MAP).remove(key);
  }

  public static void clear() {
    LOCAL.remove();
  }

}
