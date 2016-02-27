package com.jayqqaa12.jbase.sdk.util;

import java.io.IOException;
import java.util.Map;

import org.zbus.broker.Broker;
import org.zbus.broker.BrokerConfig;
import org.zbus.broker.SingleBroker;
import org.zbus.rpc.RpcException;
import org.zbus.rpc.RpcInvoker;
import org.zbus.rpc.RpcProcessor;
import org.zbus.rpc.direct.Service;
import org.zbus.rpc.direct.ServiceConfig;

import com.google.common.collect.Maps;

public class ZbusKit {
	
	public static Map<String,String> addrMaps= Maps.newHashMap();
	
	public static int DEFAULT_TIMEOUT=15* 1000;
	
	public static void setDirectRpcAddr(String key,String addr) {
		addrMaps.put(key, addr);
	}

	
	public static <T> T invokeSync(String key, Class<T> clazz,String method , Object... args) {
		
		return invokeSync(key,DEFAULT_TIMEOUT, clazz, method, args);
	}

	
	
	/***
	 * 非 HA 
	 * @param key
	 * @param clazz
	 * @param method
	 * @param args
	 * @return
	 */
	public static <T> T invokeSync(String key, int timeout,Class<T> clazz, String method, Object... args) {

		Broker broke = null;
		try {
			String addr =addrMaps.get(key);
			
			if(addr==null)  throw new RuntimeException("must set setDirectRpcAddr()  before invoke");
			
			BrokerConfig brokerConfig = new BrokerConfig();
			brokerConfig.setServerAddress(addr);
			broke = new SingleBroker(brokerConfig);
			RpcInvoker rpc = new RpcInvoker(broke);
			rpc.setTimeout(timeout);
			return rpc.invokeSync(clazz, method, args);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RpcException(e.getMessage(), e.getCause());
		} finally {
			try {
				if (broke != null) broke.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	/**
	 * 启动 driect rpc 服务器  非 HA 
	 * 
	 * 
	 * @param module
	 */
	public static void startDirectRpcService(Object... module) {

		try {
			ServiceConfig config = new ServiceConfig();
			// config.thriftServer = "0.0.0.0:25555";
			config.messageProcessor = new RpcProcessor(module);
			new Service(config).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
