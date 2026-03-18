package com.jayqqaa12.jbase.spring.task;

import java.util.concurrent.ScheduledFuture;

public class CronTask {

  private String name;
  private String cron;
  private Runnable task;
  private ScheduledFuture<?> future;

  public String getName() {
    return name;
  }

  public CronTask setName(String name) {
    this.name = name;
    return this;
  }

  public String getCron() {
    return cron;
  }

  public CronTask setCron(String cron) {
    this.cron = cron;
    return this;
  }

  public Runnable getTask() {
    return task;
  }

  public CronTask setTask(Runnable task) {
    this.task = task;
    return this;
  }

  public ScheduledFuture<?> getFuture() {
    return future;
  }

  public CronTask setFuture(ScheduledFuture<?> future) {
    this.future = future;
    return this;
  }
}
