package com.jayqqaa12.jbase.spring.boot.zookeeper.lock;

import org.apache.curator.framework.CuratorFramework;

/**
 * 分布式锁，先期使用curator的InterProcessMutex实现，后期根据需要改用自定义的lock。
 * 
 * @author Boyce 2016年6月29日 上午10:29:17
 */
public class DistributeLockFactory {

	CuratorFramework curatorFramework;

	public void setCuratorFramework(CuratorFramework curatorFramework) {
		this.curatorFramework = curatorFramework;
	}

	public static final String ZK_LOCK_ROOT = "/ZK_LOCK_ROOT";
	public static final String ZK_LOCK_ROOT_2 = ZK_LOCK_ROOT + "/";

	public DistributeLock newLock(String monitor) {
		if (!monitor.startsWith("/")) {
			monitor = ZK_LOCK_ROOT_2 + monitor;
		} else {
			monitor = ZK_LOCK_ROOT + monitor;
		}
		return new CuratorLock(curatorFramework, monitor);
	}
}
