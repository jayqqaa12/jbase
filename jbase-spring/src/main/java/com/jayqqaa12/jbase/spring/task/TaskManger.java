package com.jayqqaa12.jbase.spring.task;

import com.jayqqaa12.jbase.spring.task.config.TaskConfigLoad;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.locks.Lock;
import jakarta.annotation.PostConstruct;

/**
 * 可通过apollo 配置动态修改定时任务cron表达式
 */
public class TaskManger {

  @Autowired
  private ThreadPoolTaskScheduler schedule;
  @Autowired
  private TaskConfigLoad configLoad;
  @Autowired
  private ApplicationContext applicationContext;

  @Autowired
  private RedisLockRegistry redisLockRegistry;

  private Map<String, CronTask> taskMap = new ConcurrentHashMap<>();


  @PostConstruct
  public void initJob() {
    getMethodByAnnotation(TimerTask.class).forEach((k, v) -> initTask(k));
  }

  private void initTask(Object obj) {
    for (Method method : AopUtils.getTargetClass(obj).getMethods()) {
      TimerTask timerTask = AnnotationUtils.findAnnotation(method, TimerTask.class);
      if (timerTask == null) {
        continue;
      }
      String key = timerTask.value();
      String value = configLoad.loadCronConfig(key);

      addTask(new CronTask().setName(key)
          .setCron(value)
          .setTask(() -> {
            if (!timerTask.concurrent()) {
              lockRun(obj, method, key);
            } else {
              try {
                method.invoke(obj);
              } catch (Exception e) {
                // log error
              }
            }

          }));
    }
  }

  private void lockRun(Object obj, Method method, String key) {
    Lock lock = redisLockRegistry.obtain(key);
    boolean isLock = false;
    try {
      if (lock.tryLock()) {
        isLock = true;
        method.invoke(obj);
      }
    } catch (Exception e) {
      // log error
    } finally {
      if (isLock) {
        lock.unlock();
      }
    }
  }


  public synchronized void addTask(CronTask cronTask) {
    ScheduledFuture<?> future = schedule.schedule(cronTask.getTask(),
        new CronTrigger(cronTask.getCron()));
    cronTask.setFuture(future);
    taskMap.put(cronTask.getName(), cronTask);
  }

  public synchronized CronTask cancelTask(String name) {
    CronTask task = null;
    if (taskMap.containsKey(name)) {
      task = taskMap.get(name);
      ScheduledFuture<?> future = task.getFuture();
      future.cancel(true);
      taskMap.remove(name);
    }
    return task;
  }

  public synchronized void resetTask(String name, String cron) {
    if (StringUtils.isEmpty(cron)) {
      return;
    }
    CronTask cronTask = cancelTask(name);
    if (cronTask != null) {
      cronTask.setCron(cron);
      addTask(cronTask);
    }
  }


  public Map<Object, Set<Method>> getMethodByAnnotation(
      Class<? extends Annotation> annotation) {
    if (applicationContext == null) {
      throw new ApplicationContextException("ApplicationContext has not been set.");
    }
    Map<Object, Set<Method>> map = Maps.newHashMap();
    applicationContext.getBeansWithAnnotation(Component.class).forEach((k, v) -> {
      map.put(v, getMethodsWithAnnotation(applicationContext.getType(k), annotation));
    });
    return map;
  }


  private Set<Method> getMethodsWithAnnotation(Class<?> clazz,
      Class<? extends Annotation> annotation) {
    Set<Method> methodList = new HashSet<>();
    Method[] methods = clazz.getMethods();
    for (Method method : methods) {
      if (null != method && null != AnnotationUtils.findAnnotation(method, annotation)) {
        methodList.add(method);
      }
    }
    return methodList;
  }


}
