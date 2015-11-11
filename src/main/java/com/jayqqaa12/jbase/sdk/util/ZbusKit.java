package com.jayqqaa12.jbase.sdk.util;

import java.io.IOException;

import org.zbus.broker.Broker;
import org.zbus.broker.BrokerConfig;
import org.zbus.broker.SingleBroker;
import org.zbus.rpc.RpcException;
import org.zbus.rpc.RpcInvoker;
import org.zbus.rpc.RpcProcessor;
import org.zbus.rpc.direct.Service;
import org.zbus.rpc.direct.ServiceConfig;

public class ZbusKit {
	private static String directAddr = "127.0.0.1:15555";

	public static void setDirectRpcAddr(String addr) {
		directAddr = addr;
	}

	public static <T> T invokeSync(Class<T> clazz, String method, Object... args) {

		Broker broke = null;
		try {
			BrokerConfig brokerConfig = new BrokerConfig();
			brokerConfig.setServerAddress(directAddr);
			broke = new SingleBroker(brokerConfig);
			RpcInvoker rpc = new RpcInvoker(broke);
			rpc.setTimeout(15000);
			return rpc.invokeSync(clazz, method, args);
			
		} catch (Exception e) {
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
	 * 启动 driect rpc 服务器
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
