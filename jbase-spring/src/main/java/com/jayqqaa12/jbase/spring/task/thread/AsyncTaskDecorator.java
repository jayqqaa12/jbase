package com.jayqqaa12.jbase.spring.task.thread;

import com.jayqqaa12.jbase.spring.util.ThreadKit;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import java.util.Map;

public class AsyncTaskDecorator implements TaskDecorator {


  @Override
  public Runnable decorate(Runnable runnable) {
    Map<String, String> map = MDC.getCopyOfContextMap();
    Map<String, Object> map2 = ThreadKit.getAll();

    return () -> {
      ThreadKit.setAll(map2);
      MDC.setContextMap(map);
      try {
        runnable.run();
      } finally {
        ThreadKit.clear();
        MDC.clear();
      }
    };
  }
}
