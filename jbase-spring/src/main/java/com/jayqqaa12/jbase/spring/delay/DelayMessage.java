// Copyright 2021 Leyantech Ltd. All Rights Reserved.

package com.jayqqaa12.jbase.spring.delay;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 12
 */
public class DelayMessage<T> implements Serializable {
  public String key;
  public T body;
  public Long enqueueAt;
  public Map<String, String> traceMap;

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public T getBody() {
    return body;
  }

  public void setBody(T body) {
    this.body = body;
  }

  public Long getEnqueueAt() {
    return enqueueAt;
  }

  public void setEnqueueAt(Long enqueueAt) {
    this.enqueueAt = enqueueAt;
  }

  public Map<String, String> getTraceMap() {
    return traceMap;
  }

  public void setTraceMap(Map<String, String> traceMap) {
    this.traceMap = traceMap;
  }
}
