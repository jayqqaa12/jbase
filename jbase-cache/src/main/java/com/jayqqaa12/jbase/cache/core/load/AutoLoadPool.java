package com.jayqqaa12.jbase.cache.core.load;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AutoLoadPool {

  private Map<String, AutoLoadObject> autoLoadObjectMap = new ConcurrentHashMap<>();


  public void put(AutoLoadObject autoLoadObject) {
    String key = autoLoadObject.getKey() + '@' + autoLoadObject.getRegion();
    autoLoadObjectMap.putIfAbsent(key, autoLoadObject);

    AutoLoadObject object = autoLoadObjectMap.getOrDefault(key, autoLoadObject);
    object.setExpire(autoLoadObject.getExpire());
    object.setLastRequestTime(System.currentTimeMillis());

  }

  public void remove(String region, String key) {
    autoLoadObjectMap.remove(key + '@' + region);
  }


  public void clear() {
    autoLoadObjectMap.clear();
  }


  public Collection<AutoLoadObject> values() {

    return autoLoadObjectMap.values();
  }
}
