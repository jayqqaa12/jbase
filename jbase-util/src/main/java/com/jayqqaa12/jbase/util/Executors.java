package com.jayqqaa12.jbase.util;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

 
public class Executors {

    public static ExecutorService newFixedThreadPool(int workerNum, String poolName) {
        return java.util.concurrent.Executors.newFixedThreadPool(workerNum, new MyThreadFactory(poolName));
    }

    public static ExecutorService newFixedThreadPool(int workerNum, int maxNum, String poolName) {

        return new ThreadPoolExecutor(workerNum, maxNum,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                new MyThreadFactory(poolName));
    }


    public static ExecutorService newFixedThreadPool(int workerNum, int maxNum, String poolName, BlockingQueue workQueue) {

        return new ThreadPoolExecutor(workerNum, maxNum,
                0L, TimeUnit.MILLISECONDS,
                workQueue,
                new MyThreadFactory(poolName));
    }


    public static ScheduledExecutorService newScheduledThreadPool(int workerNum, String poolName) {
        return java.util.concurrent.Executors.newScheduledThreadPool(workerNum, new MyThreadFactory(poolName));
    }

//    public static ExecutorService newCachedThreadPool(String poolName) {
//        return java.util.concurrent.Executors.newCachedThreadPool(new MyThreadFactory(poolName));
//    }

    public static ExecutorService newSingleThreadExecutor(String poolName) {
        return java.util.concurrent.Executors.newSingleThreadExecutor(new MyThreadFactory(poolName));
    }


}

class MyThreadFactory implements ThreadFactory {
    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;

    public MyThreadFactory(String prefix) {
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        namePrefix = "THE-" + prefix + "-thd-";
    }

    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
        if (t.isDaemon())
            t.setDaemon(false);
        if (t.getPriority() != Thread.NORM_PRIORITY)
            t.setPriority(Thread.NORM_PRIORITY);
        return t;
    }
}
