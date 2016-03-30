package com.jayqqaa12.netty.session;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.collect.Maps;

/**
 * 放入 redis 就可以集群啦
 * 
 * @author 12
 *
 */
public class NettySessionManager {
	public static NettySessionManager me = new NettySessionManager();

	private static Map<String, NettySession> sessions = Maps.newConcurrentMap();

	private static final AtomicInteger connectionsCounter = new AtomicInteger(0);

	public Map<String, NettySession> getSessions() {

		return sessions;
	}

	public void addSession(String account, NettySession session) {
		if (session != null) {
			synchronized (account) {
				removeSession(account); // 防止重复 断开 之前连接的
				sessions.put(account, session);
				connectionsCounter.incrementAndGet();
			}
		}

	}

	public static NettySession getNettySession(String imei) {
		NettySession session = NettySessionManager.me.getSession(imei);
		if (session != null && session.isAction()) return session;
		else return null;
	}

	public NettySession getSession(String account) {
		return sessions.get(account);
	}

	public   void removeSession(String account) {

		synchronized (account) {
			NettySession session = sessions.remove(account);
			if (session != null) {
				session.close();
				connectionsCounter.decrementAndGet();
			}
		}
	}
}
