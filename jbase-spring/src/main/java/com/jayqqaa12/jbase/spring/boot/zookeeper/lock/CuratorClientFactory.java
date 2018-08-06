package com.jayqqaa12.jbase.spring.boot.zookeeper.lock;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author Boyce
 * 2016年6月22日 下午3:41:35 
 */
public class CuratorClientFactory {
private String zkServer;
private CuratorFramework client=null;
/**
 * @param zkServer the zkServer to set
 */
public void setZkServer(String zkServer) {
	this.zkServer = zkServer;
}
public CuratorFramework getCuratorClient(){
	if(client==null){
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
	    client = CuratorFrameworkFactory.newClient(zkServer,5000,5000, retryPolicy);
	    client.start();
	}
	return client;
}
}
