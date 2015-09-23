package com.jayqqaa12.jbase.sdk.util;

import java.io.IOException;

import org.zbus.broker.BrokerConfig;
import org.zbus.broker.SingleBroker;
import org.zbus.rpc.RpcInvoker;
import org.zbus.rpc.RpcProcessor;
import org.zbus.rpc.direct.Service;
import org.zbus.rpc.direct.ServiceConfig;

public class ZbusKit {
	public static RpcInvoker rpc;
	private static String directAddr="127.0.0.1:15555";
	
	public static void setDirectRpcAddr(String addr){
		directAddr=addr;
	}

	public static RpcInvoker getRpc() {
		if (rpc == null) {
			try {
				BrokerConfig brokerConfig = new BrokerConfig();
				brokerConfig.setServerAddress(directAddr);
				rpc = new RpcInvoker( new SingleBroker(brokerConfig));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return rpc;
	}
	
	
	/**
	 * 启动 driect rpc 服务器 
	 * 
	 * 
	 * @param module
	 */
	public static void startDirectRpcService(Object... module ){
		
		try {
			ServiceConfig config = new ServiceConfig( );
			config.thriftServer = "0.0.0.0:25555";
			config.messageProcessor = new RpcProcessor(module); 
			new Service(config).start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
