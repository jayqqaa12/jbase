package com.jayqqaa12.jbase.spring.boot.zk.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Boyce 2016年5月19日 下午5:10:38
 * 解决锁的重入问题
 */
public abstract class AbstractLock implements DistributeLock {
	private static Logger logger = LoggerFactory.getLogger(AbstractLock.class);
	String monitor;

	static ThreadLocal<Map<String, Integer>> REENTRE_COUNT = new ThreadLocal<>();

	public AbstractLock(String monitor) {
		this.monitor = monitor;
	}
	private Thread lockHolder=null;
	@Override
	public void lock() {
		Map<String, Integer> reCount = REENTRE_COUNT.get();
		if (reCount == null) {
			reCount = new HashMap<>();
			REENTRE_COUNT.set(reCount);
		}
		Integer lockCount = reCount.get(monitor);
		if (lockCount == null)
			lockCount = 0;
		if (lockCount == 0) {// 重入锁在首次lock的时候去竞争，之后只需要知道自己重入了几次就好了
			try {
				lock0();
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
			}
		} else {
			logger.debug("reentrant,no need to elect.");
		}
		lockHolder=Thread.currentThread();
		lockCount++;
		reCount.put(monitor, lockCount);
	}

	public abstract void lock0();

	public abstract void unlock0();

	@Override
	public void unlock() {
		if(Thread.currentThread()!=lockHolder){
			logger.debug("thread request unlock which is not the lockHolder,just return;");
			return;
		}
		logger.debug("unlock now");
		Map<String, Integer> reCount = REENTRE_COUNT.get();
		if (reCount == null) {
			logger.debug("no REENTRE_COUNT");
			return;
		}
		Integer lockCount = reCount.get(monitor);
		if (lockCount == null) {
			logger.debug("no REENTRE_COUNT on monitor {}", monitor);
			return;
		}
		logger.info("lock count {} decrease 1  on monitor {}", lockCount,monitor);
		lockCount--;
		reCount.put(monitor, lockCount);
		if (lockCount == 0) {
			reCount.remove(monitor);
			if (reCount.isEmpty()) {
				REENTRE_COUNT.remove();
			}
			try {
				unlock0();
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
			}
		}

	}

}
