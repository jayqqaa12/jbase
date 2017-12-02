package com.jayqqaa12.jbase.spring.boot.zk.lock;
/**
 * 不需要实现lock接口中的多数功能，加锁与解锁足够，但是一定要保证可重入性
 * @author Boyce
 * 2016年5月19日 下午3:53:28
 */
public interface DistributeLock {
	
	public void lock();
	public void unlock();
}